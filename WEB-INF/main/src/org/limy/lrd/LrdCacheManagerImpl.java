/*
 * Created 2007/01/24
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
package org.limy.lrd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.cache.LimyCache;
import org.limy.common.cache.LimyCacheManager;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdParser;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;

/**
 * Lrd用キャッシュマネージャクラスです。
 * @author Naoki Iwami
 */
public final class LrdCacheManagerImpl implements LrdCacheManager {

    // ------------------------ Constants

    /** log */
    private static final Log LOG = LogFactory.getLog(LrdCacheManagerImpl.class);

    /** 唯一のインスタンス（Dwr用） */
    private static LrdCacheManagerImpl instance;

    // ------------------------ Fields (model)

    /** LrdConfig */
    private LrdConfig config;
    
    /** Lrdマルチモデル */
    private LrdMultiModel model;

    /** Lrdコンパイルモデル */
    private LrdCompileModel compileModel;

    /** Lrdパーサ */
    private LrdParser parser;

    /** Lrdライタ */
    private LrdWriter writer;

    // ------------------------ Fields

    /** キャッシュ一覧 */
    private Map<String, LimyCache> caches = new HashMap<String, LimyCache>();    
    
    // ------------------------ Constructors
    
    public LrdCacheManagerImpl() {
        instance = this; // BAD CODING!!
    }
    
    public static LrdCacheManagerImpl getInstance() {
        return instance;
    }
    
    // ------------------------ Bind Setter Methods (framework)

    /**
     * LrdConfigを設定します。
     * @param config LrdConfig
     */
    public void setConfig(LrdConfig config) {
        this.config = config;
    }

    /**
     * Lrdマルチモデルを設定します。
     * @param model Lrdマルチモデル
     */
    public void setModel(LrdMultiModel model) {
        this.model = model;
    }

    /**
     * Lrdパーサを設定します。
     * @param parser Lrdパーサ
     */
    public void setParser(LrdParser parser) {
        this.parser = parser;
    }

    /**
     * Lrdライタを設定します。
     * @param writer Lrdライタ
     */
    public void setWriter(LrdWriter writer) {
        this.writer = writer;
    }

    /**
     * Lrdコンパイルモデルを設定します。
     * @param compileModel Lrdコンパイルモデル
     */
    public void setCompileModel(LrdCompileModel compileModel) {
        this.compileModel = compileModel;
    }

    // ------------------------ Public Methods
    
    /**
     * ダミー用のLrdBeanを生成して返します。
     * @param repositoryId リポジトリID
     * @param contents コンテンツ内容
     * @return LrdBean
     * @throws IOException I/O例外
     */
    public LrdBean createDummyBean(
            String repositoryId, String contents) throws IOException {
        
        BufferedReader reader = new BufferedReader(new StringReader(contents));
        LrdBean bean = parser.parse(model.getRepository(repositoryId),
                new LrdPath(model.getProject(repositoryId), ".dummy"), reader);
        reader.close();
        return bean;
    }

    /**
     * LrdBeanを生成して返します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdパス
     * @param contents コンテンツ内容
     * @param saveCache 生成した結果をキャッシュに格納するかどうか
     * @return LrdBean
     * @throws IOException I/O例外
     */
    public LrdBean createBean(
            String repositoryId,
            String lrdPath, String contents, boolean saveCache) throws IOException {
        
        BufferedReader reader = new BufferedReader(new StringReader(contents));
        LrdBean bean = parser.parse(model.getRepository(repositoryId),
                new LrdPath(model.getProject(repositoryId), lrdPath), reader);
        reader.close();
        if (saveCache) {
            getCache(repositoryId).put("//LrdBean//" + lrdPath, bean);
        }
        return bean;
    }

    /**
     * LrdBeanを返します（キャッシュ使用）。
     * <p>
     * キャッシュに存在しなかった場合は作成して返します。
     * </p>
     * @param repositoryId リポジトリID
     * @param info Lrd内容
     * @return LrdBean
     * @throws IOException I/O例外
     */
    public LrdBean getBean(String repositoryId,
            CommitFileInfo info) throws IOException {
        
        String lrdPath = info.getPath();
        LrdBean lrdBean = (LrdBean)getCache(repositoryId).get("//LrdBean//" + lrdPath);
        if (lrdBean == null) {
            lrdBean = createBean(repositoryId,
                    lrdPath, info.getContentStr(), true);
        }
        return lrdBean;
    }

    /**
     * ディレクトリのインデックスファイルタイトルを返します（キャッシュ有）。
     * @param repositoryId リポジトリID
     * @param path ディレクトリのパス名
     * @return タイトル
     * @throws IOException I/O例外
     */
    public String getIndexLrdTitle(String repositoryId,
            LrdPath path) throws IOException {
        String title = (String)getCache(repositoryId).get("//indexTitle//" + path);
        if (title == null) {
            String relativePath = path.createNewPath("index.lrd").getRelativePath();
            try {
                CommitFileInfo lrdInfo = model.getRepository(repositoryId).getRepositoryFile(
                        relativePath);
                LrdBean bean = getBean(repositoryId, lrdInfo);
                title = bean.getTitle();
                if (title == null) {
                    if (path.getName().length() == 0) {
                        title = "*ROOT*";
                    } else {
                        title = path.getName();
                    }
                }
            } catch (LrdException e) {
                LOG.warn(relativePath + " is not found!");
                title = path.getBaseName();
            }
        }
        if (title == null) {
            title = path.getName();
        }
        getCache(repositoryId).put("//indexTitle//" + path, title);
        return title;
    }

    public void clearLrdCache(String repositoryId, String lrdPath) throws IOException {
        getCache(repositoryId).remove("//LrdBean//" + lrdPath);
        
        String cacheKey = null;
        if ("index.lrd".equals(lrdPath)) {
            cacheKey = "//indexTitle//";
        }
        if (lrdPath.endsWith("/index.lrd")) {
            cacheKey = "//indexTitle//" + lrdPath.substring(0, lrdPath.length() - 10);
        }
        
        if (cacheKey != null) {
            String title = (String)getCache(repositoryId).get(cacheKey);
            // TODO キャッシュに無い状態で呼ばれるとうまく動作しない
            LrdBean bean = getBean(repositoryId, new CommitFileInfo(
                    lrdPath, new byte[0], null));
            if (!StringUtils.equals(bean.getTitle(), title)) {
                // index.lrdのタイトルが変わったらメニューツリーキャッシュ削除
                clearMenuTreeCache(repositoryId);
            }
            getCache(repositoryId).remove(cacheKey);
        }
    }
    
    public void clearMenuTreeCache(String repositoryId) {
        getCache(repositoryId).removeTree("//menuTree//");
        getCache(repositoryId).removeTree("//menuContents//");
    }
    
    public LrdNode getMenuTree(String repositoryId,
            LrdBean bean) throws LrdException, IOException {
        
        LrdNode menuRoot = (LrdNode)getCache(repositoryId).get(
                "//menuTree//" + bean.getPath().getRelativePath());
        if (menuRoot == null) {
            menuRoot = model.getRepository(repositoryId).getDirectoryRoot();
            menuRoot = createOnlyTargetTree(repositoryId,
                    menuRoot, bean.getPath());
            if (repositoryId.length() > 0) {
                // ダミーのときはキャッシュに入れない
                getCache(repositoryId).put(
                        "//menuTree//" + bean.getPath().getRelativePath(), menuRoot);
            }
        }
        return menuRoot;
    }

    public String getMenuContents(String repositoryId,
            LrdBean bean) throws LrdException, IOException {
        
        String menuContents = (String)getCache(repositoryId).get("//menuContents//"
                + bean.getPath().getRelativePath());
        if (menuContents == null) {
            LrdNode menuNode = compileModel.createMenuTree(repositoryId, bean);
            menuContents = writer.getMenuContents(
                    config.getRepositoryBean(repositoryId), bean, menuNode);
            getCache(repositoryId).put(
                    "//menuContents//" + bean.getPath().getRelativePath(), menuContents);
        }
        return menuContents;
    }
    
    // ------------------------ Private Methods

    /**
     * 対象パス以外のノードを取り除いたメニュー表示用ノードを作成します。
     * @param repositoryId リポジトリID
     * @param root ノードツリー
     * @param targetPath 対象パス
     * @return 対象以外のノードを取り除いたノード
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    private LrdNode createOnlyTargetTree(
            String repositoryId,
            LrdNode root, LrdPath targetPath)
            throws LrdException, IOException {
        
        LrdNode result = root.deepCopy();
        adjustMenuNodes(repositoryId, result, targetPath);
        return result;
    }

    /**
     * ノードから対象パス以外のノードを無効化します（再帰）。
     * @param repositoryId リポジトリID
     * @param root ルートノード
     * @param targetPath 対象パス
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    private void adjustMenuNodes(String repositoryId, LrdNode root, LrdPath targetPath)
            throws LrdException, IOException {
        
        if (root.isFolder()) {
            if (root.searchSubNode("index.lrd") == null) {
                // index.lrd が見つからないディレクトリは無効にする
                root.setEnable(false);
            } else {
                // index.lrdの内容に沿って子を並び替え
                sortSubNodes(repositoryId, root);
            }
            for (LrdNode child : root.getSubNodes()) {
                adjustMenuNodes(repositoryId, child, targetPath);
            }
        } else {
            if (!root.getPath().getBaseName().equals(targetPath.getBaseName())) {
                root.setEnable(false);
            }
        }
    }

    /**
     * ディレクトリ内の全ノードを、index.lrdに記述された順に並び替えます。
     * @param repositoryId リポジトリID
     * @param root ディレクトリノード
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    private void sortSubNodes(String repositoryId, LrdNode root)
            throws LrdException, IOException {
        
        Collection<String> urls = getInnerUrls(repositoryId,
                root.searchSubNode("index.lrd").getPath().getRelativePath());
        
        List<LrdNode> sortedNodes = new ArrayList<LrdNode>();
        for (String url : urls) {
            // リンク先ノードをツリー内で検索
            LrdNode node = root.searchSubNodeDepth(url);
            if (node != null) {
                // リンク先ノードを一旦ツリーから削除
                if (!root.getRawSubNodes().remove(node)) {
                    node = root.searchSubNode(url.substring(0, url.indexOf('/')));
                    root.getRawSubNodes().remove(node);
                }
                sortedNodes.add(node);
            }
        }
        
        // index.lrdに記述されなかったリンク先をまずツリーに追加
        sortedNodes.addAll(root.getRawSubNodes());
        
        // 一旦削除したリンク先ノードをツリーに追加（index.lrdに記述された順で）
        root.setSubNodes(sortedNodes);
    }

    /**
     * Lrdファイル内の全リンクを取得します。
     * @param repositoryId リポジトリID
     * @param repositoryPath Lrdファイルのリポジトリパス
     * @return 全リンク
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    private Collection<String> getInnerUrls(
            String repositoryId,
            String repositoryPath)
            throws LrdException, IOException {
        
        CommitFileInfo lrdInfo = model.getRepository(repositoryId).getRepositoryFile(
                repositoryPath);
        LrdBean bean = getBean(repositoryId, lrdInfo);
        return bean.createInnerUrls();
    }

    private LimyCache getCache(String repositoryId) {
        LimyCache result = caches.get(repositoryId);
        if (result == null) {
            result = LimyCacheManager.getCache(this.getClass().getName() + "." + repositoryId);
            caches.put(repositoryId, result);
        }
        return result;
    }

}
