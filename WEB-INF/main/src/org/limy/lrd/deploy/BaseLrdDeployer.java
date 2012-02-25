/*
 * Created 2007/08/15
 * Copyright (C) 2003-2007  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limyweb-lrd.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.limy.lrd.deploy;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.UrlUtils;
import org.limy.common.web.LimyWebSession;
import org.limy.common.web.WebworkUtils;
import org.limy.lrd.TargetInfoCreator;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.DeployInfo;
import org.limy.lrd.common.bean.LrdNode;

/**
 * Lrdデプロイを担当する基底クラスです。
 * @author Naoki Iwami
 */
public class BaseLrdDeployer {
    
    // ------------------------ Fields

    /** 参照情報作成担当 */
    private TargetInfoCreator infoCreator;

    /** Lrdコンパイラ */
    private LrdCompiler compiler;

    /** キャッシュマネージャ */
    private LrdCacheManager cacheManager;
    
    // ------------------------ Public Methods
    
    // ------------------------ Protected Methods

    /**
     * HTML出力用のコンテキストを作成します。
     * @param bean リポジトリBean
     * @param url 基準URL
     * @return コンテキスト
     */
    protected Context createContext(RepositoryBean bean, String url) {
        Context context = new VelocityContext();
        
        context.put("baseHref", url); // デプロイ先のROOT
        context.put("resourceUrlBase", url); // リソース参照ROOT
        
        context.put("baseUrl",  WebworkUtils.getContextRootUrl()); // サーバROOT
        context.put("enableComment", Boolean.valueOf(bean.isEnableComment()));
        
        if (url.indexOf('/', 7) >= 0) {
            // http://sample.org/aaa
            context.put("targetDomain", url.substring(7, url.indexOf('/', 7)));
        } else {
            // http://sample.org
            context.put("targetDomain", url.substring(7));
        }

        return context;
    }
    
    /**
     * staticファイルをデプロイします。
     * @param repositoryBean リポジトリBean
     * @param deployable デプロイ担当
     * @param force 強制デプロイフラグ
     * @return デプロイしたファイル数
     * @throws IOException I/O例外
     */
    protected int deployStaticFiles(RepositoryBean repositoryBean,
            Deployable deployable, boolean force) throws IOException {
        if (force) {
            deployable.deleteDir("style");
            deployable.deleteDir("images");
            deployable.deleteDir("script");
        }
        deployable.deployStaticFiles(
                new File(repositoryBean.getLocalContentFile(), ".template/style"),
                "style");
        deployable.deployStaticFiles(WebworkUtils.getFile("images"), "images");
        deployable.deployStaticFiles(WebworkUtils.getFile("script"), "script");
        int result = WebworkUtils.getFile("style").list(FileFileFilter.FILE).length
                + WebworkUtils.getFile("images").list(FileFileFilter.FILE).length;
        sendProgress(deployable, result);
        return result;
    }
    
    /**
     * Lrdファイルおよび依存ファイルをデプロイします。
     * @param repositoryBean リポジトリBean
     * @param project Lrdプロジェクト
     * @param lrdPath Lrdパス
     * @param context コンテキスト情報
     * @param deployable デプロイ担当
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    protected void deployLrdRecursive(
            RepositoryBean repositoryBean,
            String lrdPath,
            Context context,
            Deployable deployable)
            throws LrdException, IOException {

        String repositoryId = repositoryBean.getId();

        // SVNからファイルを読む（キャッシュがあればそれを使用）
        CommitFileInfo info = repositoryBean.getRepository().getRepositoryFile(lrdPath);

        if (!lrdPath.endsWith(".lrd")) {
            // Lrd以外のファイルはそのままデプロイ
            deployable.deployFile(lrdPath, info.getContents());
            return;
        }
        
        // 読み込んだLrdテキスト内容からLrdBeanを生成（キャッシュは使わない）
        LrdBean bean = cacheManager.createBean(
                repositoryId, lrdPath,  info.getContentStr(), true);
        // LrdBeanから参照Beanを全て読み込んでリンク先情報を生成
        Collection<LrdTargetInfo> extInfos = infoCreator.createTargetInfos(repositoryBean, bean);
        // 生成したLrdBeanを参照情報と共に渡してHTMLのデプロイを行う
        deployLrdHtml(repositoryBean, bean, extInfos, context, deployable);
        
        // 全依存ファイルをデプロイ
        for (String path : bean.getDependPaths()) {
            try {
                String childPath = UrlUtils.concatUrl(UrlUtils.getParent(lrdPath), path);
                deployLrdRecursive(repositoryBean, childPath, context, deployable);
            } catch (LrdException e) {
                // do nothing
            }
        }
        
    }

    /**
     * 全デプロイ時に転送する全ファイル数を返します。
     * @param repositoryBean リポジトリBean
     * @return 全ファイル数
     * @throws LrdException 
     */
    protected int countAllDeployFiles(RepositoryBean repositoryBean) throws LrdException {
        int result = 0;
        result += WebworkUtils.getFile("style").list(FileFileFilter.FILE).length;
        result += WebworkUtils.getFile("images").list(FileFileFilter.FILE).length;
        LrdNode root = repositoryBean.getRepository().getDirectoryRoot();
        result += countNodeNumber(root);
        return result;
    }

    /**
     * ノードツリーに含まれる全ノード（Lrdファイル）をデプロイします。
     * @param repositoryBean リポジトリBean
     * @param context ンテキスト情報
     * @param deployable デプロイ担当
     * @param root ノードまたはノードツリー
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    protected void deployLrdAllFiles(RepositoryBean repositoryBean,
            Context context, Deployable deployable,
            LrdNode root) throws LrdException, IOException {
        
        if (root.isFolder()) {
            for (LrdNode node : root.getSubNodes()) {
                deployLrdAllFiles(repositoryBean, context, deployable, node);
            }
        } else {
            deployLrdRecursive(repositoryBean, root.getPath().getRelativePath(),
                    context, deployable);
            sendProgress(deployable, 1);
        }
    }

    // ------------------------ Private Methods
    
    private void sendProgress(Deployable deployable, int number) {
        LimyWebSession session = deployable.getSession();
        if (session != null) {
            deployable.addDeployCount(number);
            session.sendJavaScript("comet_work("
                    + deployable.getDeployCount() + ", " + deployable.getAllDeployCount() + ")");
        }
    }

    /**
     * Lrdファイルをフレーム形式でコンパイルしてHTMLファイルのみデプロイします。
     * @param repositoryBean リポジトリBean
     * @param bean LrdBean
     * @param extInfos リンク先情報
     * @param context コンテキスト情報
     * @param deployable デプロイ担当
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    private void deployLrdHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Collection<LrdTargetInfo> extInfos, Context context,
            Deployable deployable)
            throws LrdException, IOException {
        
        // Lrdをフレーム形式でコンパイル
        String outHtml = compiler.compileToHtml(repositoryBean, bean, extInfos, context);
        String path = bean.getPath().getRelativePath();
        
        // デプロイ
        deployable.deployLrd(path, outHtml);
    }

    /**
     * ノードツリーに含まれる全ノード数を返します。
     * @param root ノードまたはノードツリー
     * @return 全ノード数
     */
    private int countNodeNumber(LrdNode root) {
        if (root.isFolder()) {
            int cnt = 0;
            for (LrdNode node : root.getSubNodes()) {
                cnt += countNodeNumber(node);
            }
            return cnt;
        }
        return 1;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * 参照情報作成担当を取得します。
     * @return 参照情報作成担当
     */
    public TargetInfoCreator getInfoCreator() {
        return infoCreator;
    }

    /**
     * 参照情報作成担当を設定します。
     * @param infoCreator 参照情報作成担当
     */
    public void setInfoCreator(TargetInfoCreator infoCreator) {
        this.infoCreator = infoCreator;
    }

    /**
     * Lrdコンパイラを取得します。
     * @return Lrdコンパイラ
     */
    public LrdCompiler getCompiler() {
        return compiler;
    }

    /**
     * Lrdコンパイラを設定します。
     * @param compiler Lrdコンパイラ
     */
    public void setCompiler(LrdCompiler compiler) {
        this.compiler = compiler;
    }

    /**
     * キャッシュマネージャを取得します。
     * @return キャッシュマネージャ
     */
    public LrdCacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * キャッシュマネージャを設定します。
     * @param cacheManager キャッシュマネージャ
     */
    public void setCacheManager(LrdCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
