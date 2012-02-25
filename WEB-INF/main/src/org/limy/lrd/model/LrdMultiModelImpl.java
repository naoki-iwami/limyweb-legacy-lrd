/*
 * Created 2007/07/29
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
package org.limy.lrd.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.annotations.RemoteMethod;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.UrlUtils;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.LrdCompileModel;
import org.limy.lrd.LrdConfig;
import org.limy.lrd.LrdMultiModel;
import org.limy.lrd.bean.RecentEntry;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdDwrResult;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.common.bean.LrdDeployInfo.DeployType;
import org.limy.lrd.deploy.LrdDeployer;

/**
 * @author Naoki Iwami
 */
public class LrdMultiModelImpl implements LrdMultiModel {

    // ------------------------ Fields (model)

    /** LrdConfig */
    private LrdConfig config;

    /** コンパイルモデル */
    private LrdCompileModel compileModel;
    
    /** キャッシュマネージャ */
    private LrdCacheManager cacheManager;
    
    /** ローカルデプロイ担当 */
    private LrdDeployer localDeployer;

    /** FTPデプロイ担当 */
    private LrdDeployer ftpDeployer;

    // ------------------------ Bind Setter Methods (framework)

    /**
     * LrdConfigを設定します。
     * @param config LrdConfig
     */
    public void setConfig(LrdConfig config) {
        this.config = config;
    }

    /**
     * コンパイルモデルを設定します。
     * @param compileModel コンパイルモデル
     */
    public void setCompileModel(LrdCompileModel compileModel) {
        this.compileModel = compileModel;
    }

    /**
     * キャッシュマネージャを設定します。
     * @param cacheManager キャッシュマネージャ
     */
    public void setCacheManager(LrdCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
     * ローカルデプロイ担当を設定します。
     * @param localDeployer ローカルデプロイ担当
     */
    public void setLocalDeployer(LrdDeployer localDeployer) {
        this.localDeployer = localDeployer;
    }

    /**
     * FTPデプロイ担当を設定します。
     * @param ftpDeployer FTPデプロイ担当
     */
    public void setFtpDeployer(LrdDeployer ftpDeployer) {
        this.ftpDeployer = ftpDeployer;
    }

    // ------------------------ Implement Methods

    /**
     * リポジトリ上に空ファイルを作成します。
     * @param repositoryId リポジトリID
     * @param lrdPath 作成パス
     * @throws LrdException 共通例外
     */
    @RemoteMethod
    public void createFile(String repositoryId, String lrdPath)
            throws LrdException {
        
        getRepository(repositoryId).addFile(lrdPath);
        cacheManager.clearMenuTreeCache(repositoryId);
    }

    /**
     * 指定したLrdパスのコンテンツ内容を返します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdパス
     * @return Lrdコンテンツ内容
     * @throws LrdException 共通例外
     */
    public LrdDwrResult changeLocation(String repositoryId, String lrdPath)
            throws LrdException {
   
        String content = getRepository(repositoryId).getRepositoryFile(lrdPath).getContentStr();
        return new LrdDwrResult(content);
    }

    /**
     * Lrdファイルをコミットします。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @param contents Lrdコンテンツ内容
     * @return コンパイル結果
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public LrdDwrResult commit(String repositoryId, String lrdPath, String contents)
            throws IOException, LrdException {
        
        LrdDwrResult result = compileModel.compile(repositoryId, lrdPath, contents);
        cacheManager.clearLrdCache(repositoryId, lrdPath);
        
        List<CommitFileInfo> commits = new ArrayList<CommitFileInfo>();
        appendCommitInfos(repositoryId,
                lrdPath, contents, commits, result);
        getRepository(repositoryId).commitMultiFile(
                commits.toArray(new CommitFileInfo[commits.size()]));
        return result;
    }

    /**
     * リポジトリまたはキャッシュの内容からLrdBeanを作成して返します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdパス
     * @return LedBean
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    public LrdBean getBean(String repositoryId, String lrdPath) throws LrdException, IOException {
        
        CommitFileInfo info = getRepository(repositoryId).getRepositoryFile(
                lrdPath);
        return cacheManager.getBean(repositoryId, info);
    }

    /**
     * Lrdファイルをデプロイします。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @param deployId デプロイID
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public void deploy(String repositoryId, String lrdPath, String deployId)
            throws IOException, LrdException {
        
        Collection<LrdDeployInfo> deployInfos = getRepositoryBean(repositoryId).getDeployInfos();
        for (LrdDeployInfo deployInfo : deployInfos) {
            if (deployInfo.getId().equals(deployId)) {
                if (deployInfo.getType() == DeployType.LOCAL) {
                    localDeployer.deploySingleLrd(config.getRepositoryBean(repositoryId),
                            lrdPath, deployInfo);
                    
                } else {
                    ftpDeployer.deploySingleLrd(config.getRepositoryBean(repositoryId),
                            lrdPath, deployInfo);
                }
                break;
            }
        }
    }

    /**
     * Lrdファイルを全デプロイします。
     * @param repositoryId リポジトリID
     * @param deployId デプロイID
     * @param session Webセッション
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public void deployAll(String repositoryId, String deployId,
            LimyWebSession session) throws IOException,
            LrdException {
        
        Collection<LrdDeployInfo> deployInfos = getRepositoryBean(repositoryId).getDeployInfos();
        for (LrdDeployInfo deployInfo : deployInfos) {
            
            if (deployInfo.getId().equals(deployId)) {
                if (deployInfo.getType() == DeployType.LOCAL) {
                    localDeployer.deployAllLrd(config.getRepositoryBean(repositoryId),
                            session, deployInfo);
                } else {
                    ftpDeployer.deployAllLrd(config.getRepositoryBean(repositoryId),
                            session, deployInfo);
                }
                break;
            }
        }
    }

    public void deployRecentPage(String repositoryId, String deployId) throws LrdException, IOException {
        String html = compileModel.getRecentEntryHtml(repositoryId, deployId);
        
        Collection<LrdDeployInfo> deployInfos = getRepositoryBean(repositoryId).getDeployInfos();
        for (LrdDeployInfo deployInfo : deployInfos) {
            if (deployInfo.getType() == DeployType.LOCAL) {
                localDeployer.deployStaticFile(getRepositoryBean(repositoryId),
                        "rightbar.html", html.getBytes("UTF-8"));
            }
        }

    }
    
//    /**
//     * LrdBeanをコンパイルしてHTML形式の出力を返します。
//     * @param repositoryId リポジトリID
//     * @param bean LrdBean
//     * @return HTML形式文字列
//     * @throws IOException I/O例外
//     */
//    public String getContentString(String repositoryId, LrdBean bean) throws IOException {
//        return lrdWriter.createHtmlString(bean, null);
//    }
//
//    public String getMenuContents(String repositoryId, LrdBean bean) throws LrdException,
//            IOException {
//        return cacheManager.getMenuContents(repositoryId, bean);
//    }
//
//    public LrdVelocityMacro getVelocityMacro(String repositoryId) {
//        return new LrdVelocityMacro(this, repositoryId);
//    }

    /**
     * リポジトリBeanを取得します。
     * @param repositoryId リポジトリID
     * @return リポジトリBean
     */
    public RepositoryBean getRepositoryBean(String repositoryId) {
        return config.getRepositoryBean(repositoryId);
    }

    /**
     * Lrdプロジェクトを取得します。
     * @param repositoryId リポジトリID
     * @return Lrdプロジェクト
     */
    public LrdProject getProject(String repositoryId) {
        return getRepositoryBean(repositoryId).getProject();
    }

    /**
     * Lrdリポジトリを取得します。
     * @param repositoryId リポジトリID
     * @return Lrdリポジトリ
     */
    public LrdRepository getRepository(String repositoryId) {
        return getRepositoryBean(repositoryId).getRepository();
    }
    
//    public File getLocalContentFile(String repositoryId) throws IOException {
//        return config.getLocalContentFile(repositoryId);
//    }

    // ------------------------ Private Methods

    /**
     * Lrdファイルの内容をコミット情報格納先に追加します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdパス
     * @param contents Lrdコンテンツ内容
     * @param commits コミット情報格納先
     * @param result コンパイル結果格納先
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    private void appendCommitInfos(
            String repositoryId,
            String lrdPath, String contents,
            List<CommitFileInfo> commits, LrdDwrResult result)
            throws IOException, LrdException {
        
        RepositoryBean repositoryBean = getRepositoryBean(repositoryId);
        
        // Lrdファイル自身を追加
        commits.add(new CommitFileInfo(lrdPath,
                contents.getBytes(repositoryBean.getRepositoryCharset()),
                repositoryBean.getRepositoryCharset()));
        
        LrdBean bean = cacheManager.createBean(repositoryId, lrdPath, contents, true);
        if (!StringUtils.isEmpty(bean.getAlertString())) {
            result.setAlertString(bean.getAlertString());
        }

        String targetDir = UrlUtils.getParent(lrdPath);
        // 依存ファイルをループ
        for (String path : bean.getDependPaths()) {
            String targetFile = UrlUtils.concatUrl(targetDir, path);
            if (targetFile.endsWith(".lrd")) {
                // 依存ファイルがLrdファイルの場合、
                // そのファイルも同一チェンジセット内でコミットする（SVNで有効）
                String subContents = getRepository(repositoryId).getRepositoryFile(
                        targetFile).getContentStr();
                appendCommitInfos(repositoryId,
                        targetFile, subContents, commits, result);
            }
        }
    }


}
