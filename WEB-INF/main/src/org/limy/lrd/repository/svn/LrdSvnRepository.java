/*
 * Created 2006/09/02
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
package org.limy.lrd.repository.svn;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.limy.common.cache.LimyCache;
import org.limy.common.cache.LimyCacheManager;
import org.limy.common.repository.RepositoryInfo;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.svn.SvnUtils;
import org.limy.lrd.bean.LrdLightBean;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNInfo;

import com.sun.corba.se.impl.oa.toa.TOA;

/**
 * SVNリポジトリクラスです。
 * @author Naoki Iwami
 */
public final class LrdSvnRepository implements LrdRepository {
    
    // ------------------------ Fields

    /** リポジトリBean */
    private final RepositoryBean repositoryBean;
    
    /**
     * ルートノード
     */
    private LrdNode lrdRoot;

    /**
     * キャッシュ
     */
    private LimyCache cache;

    /**
     * ファイル更新フラグ（全メニュー作成用）
     */
    private boolean dirty;
    
    // ------------------------ Constructors

    public LrdSvnRepository(RepositoryBean bean) {
        this.repositoryBean = bean;
        cache = LimyCacheManager.getCache(this.getClass().getName() + "." + bean.getId());
    }
    
    // ------------------------ Public Methods

    /**
     * ファイルの内容を取得します（キャッシュ付）。
     * @param lrdPath ファイルパス
     * @return ファイル内容
     * @throws LrdException 共通例外
     */
    public CommitFileInfo getRepositoryFile(String lrdPath)
            throws LrdException {
        
        CommitFileInfo info = (CommitFileInfo)cache.get("//contents//" + lrdPath);
        if (info == null) {
            try {
                info = getSvnRepositoryContents(new String[] { lrdPath })[0];
            } catch (SVNException e) {
                throw new LrdException(e);
            }
            cache.put("//contents//" + lrdPath, info);
            
        }
        return info;
    }
    
    /**
     * 複数ファイルの内容を取得します（キャッシュ付）。
     * @param lrdPaths ファイルパス一覧
     * @return ファイル内容一覧
     * @throws LrdException 共通例外
     */
    public CommitFileInfo[] getRepositoryFiles(
            String[] lrdPaths) throws LrdException {
        
        Collection<CommitFileInfo> results = new ArrayList<CommitFileInfo>();
        for (String lrdPath : lrdPaths) {
            results.add(getRepositoryFile(lrdPath));
        }
        return results.toArray(new CommitFileInfo[results.size()]);
    }

    /**
     * Lrdプロジェクトのルートノードを返します。
     * @return ルートノード
     * @throws LrdException 共通例外
     */
    public LrdNode getDirectoryRoot() throws LrdException {
        if (lrdRoot != null) {
            lrdRoot.setAllEnable(true);
            LrdNode node = lrdRoot.searchSubNode(".template");
            if (node != null) {
                node.setEnable(false);
            }
            return lrdRoot;
        }
        
        lrdRoot = SvnkitUtils.getRepositoryRootNode(repositoryBean);
        return lrdRoot;
    }

    /**
     * パス直下にある全ファイルを取得します。
     * @param path パス
     * @return パス直下にある全ファイル（ディレクトリは除く）
     * @throws LrdException 共通例外
     */
    public LrdPath[] getFilesFromDirectory(String path) throws LrdException {
        LrdNode now = getNodeByPath(path);
        
        List<LrdPath> results = new ArrayList<LrdPath>();
        for (LrdNode node : now.getSubNodes()) {
            if (!node.isFolder()) {
                results.add(node.getPath());
            }
        }
        return results.toArray(new LrdPath[results.size()]);
    }
    
    public boolean isDirectory(String path) throws LrdException {
        LrdNode now = getNodeByPath(path);
        return now.isFolder();
    }

    /**
     * SVNリポジトリを取得します。
     * @return SVNリポジトリ
     * @throws SVNException SVN例外
     */
    public SVNRepository getSvnRepository() throws SVNException {
        RepositoryInfo repositoryInfo = repositoryBean.getRepositoryInfo();
        SVNRepository repository = SvnUtils.getRepository(
                repositoryInfo.getRepositoryUrl(),
                repositoryInfo.getAuthUser(), repositoryInfo.getAuthPass());
        return repository;
    }


    public Date getTimestamp(
            String lrdPath, String authUser, String authPass)
            throws LrdException {
        
        try {
            SVNInfo svnInfo = getSvnInfo(lrdPath, authUser, authPass);
            return new Date(svnInfo.getCommittedDate().getTime());
        } catch (SVNException e) {
            throw new LrdException(e);
        }
    }

    /**
     * SVNリポジトリに空ファイルを追加します。
     * @param targetUrl リポジトリルートからの相対パス
     * @throws SVNException SVN例外
     */
    public void addFile(String targetUrl) throws LrdException {
        
        try {
            SvnUtils.addFile(getSvnRepository(), targetUrl, new byte[0], "");
        } catch (SVNException e) {
            throw new LrdException(e);
        }
        if (lrdRoot != null) {
            // 初回空リポジトリ作成時のみ lrdRoot は空
            addNode(targetUrl);
        }
    }


    public void addDirectory(String targetUrl) throws LrdException {
        try {
            SvnUtils.addFile(getSvnRepository(), targetUrl, null, "");
        } catch (SVNException e) {
            throw new LrdException(e);
        }
        if (lrdRoot != null) {
            // 初回空リポジトリ作成時のみ lrdRoot は空
            addNode(targetUrl);
        }
    }
    
    public void commitMultiFile(CommitFileInfo[] infos) throws LrdException {
        
        try {
            SvnUtils.modifyMultiFile(getSvnRepository(), infos, "");
        } catch (SVNException e) {
            throw new LrdException(e);
        }
        for (CommitFileInfo info : infos) {
            cache.put("//contents//" + info.getPath(),
                    new CommitFileInfo(info.getPath(), info.getContents(),
                            repositoryBean.getRepositoryCharset()));
            cache.remove("//info//" + info.getPath());
            dirty = true;
        }
    }

    public void addAndcommitMultiFile(CommitFileInfo[] commitInfos)
            throws LrdException {
        
        try {
            SvnUtils.addMultiFile(getSvnRepository(), commitInfos, "");
        } catch (SVNException e) {
            throw new LrdException(e);
        }
        for (CommitFileInfo info : commitInfos) {
            addNode(info.getPath());
        }

    }

    /**
     * 全メニュー情報をHTML形式で取得します。
     * @param repositoryBean リポジトリBean
     * @param writer メニュー出力Writer
     * @return 全メニュー情報
     * @throws LrdException 共通例外
     */
    public String getAllMenu(LrdWriter writer) throws LrdException {
        
        String result = (String)cache.get("//allMenu//");
        
        if (result == null || dirty) {
            synchronized (LrdSvnRepository.class) {
                LrdNode root = getDirectoryRoot();
                StringWriter out = new StringWriter();
                try {
                    writer.writeAllMenu(repositoryBean, out, root);
                } catch (IOException e) {
                    throw new LrdException(e);
                }
                result = out.toString();
                cache.put("//allMenu//", result);
                dirty = false;
            }
        }
        return result;
    }

    public Collection<CommitFileInfo> getRecentUpdateFiles(int count) throws LrdException {
        try {
            CommitFileInfo[] infos = SvnUtils.getRecentCommitedEntries(
                    repositoryBean.getUserName(),
                    repositoryBean.getPassword(),
                    repositoryBean.getUrl(), count);
            
            Collection<String> paths = new ArrayList<String>();
            for (CommitFileInfo info : infos) {
                paths.add(info.getPath());
            }
            CommitFileInfo[] repositoryFiles = getRepositoryFiles(paths.toArray(new String[paths.size()]));
            return Arrays.asList(repositoryFiles);
        } catch (SVNException e) {
            throw new LrdException(e);
        }
    }
    
    // ------------------------ Private Methods

    /**
     * SVNリポジトリから複数ファイルの内容を取得します。
     * @param project Lrdプロジェクト
     * @param lrdPaths ファイルパス一覧
     * @return ファイル内容一覧
     * @throws SVNException SVN例外
     */
    private CommitFileInfo[] getSvnRepositoryContents(
            String[] lrdPaths) throws SVNException {
        
        RepositoryInfo repositoryInfo = repositoryBean.getRepositoryInfo();
        CommitFileInfo[] infos = SvnUtils.getMultiRepositoryContents(
                repositoryInfo.getAuthUser(),
                repositoryInfo.getAuthPass(),
                repositoryInfo.getRepositoryUrl(), lrdPaths,
                repositoryBean.getRepositoryCharset());
        for (CommitFileInfo info : infos) {
            cache.put("//contents//" + info.getPath(), info);
        }
        return infos;
    }

    /**
     * SVNからリポジトリ情報を取得します（キャッシュ付）。
     * @param lrdPath Lrdパス
     * @param authUser 認証ユーザ名
     * @param authPass 認証パスワード
     * @return リポジトリ情報
     * @throws SVNException SVN例外
     */
    private SVNInfo getSvnInfo(
            String lrdPath, String authUser, String authPass)
            throws SVNException {
        
        SVNInfo svnInfo;
        String key = "//info//" + lrdPath;
        svnInfo = (SVNInfo)cache.get(key);
        if (svnInfo != null) {
            return svnInfo;
        }
        
        svnInfo = SvnUtils.getSvnInfo(
                repositoryBean.getRepositoryInfo().getRepositoryUrl() + lrdPath,
                authUser, authPass);
        cache.put(key, svnInfo);
        return svnInfo;
    }

    /**
     * Lrdプロジェクト内ツリーから対象パスのノードを抽出して返します。
     * @param path 対象パス
     * @return ノード
     * @throws LrdException 共通例外
     */
    private LrdNode getNodeByPath(String path) throws LrdException {
        StringTokenizer tokenizer = new StringTokenizer(path, "/");
        LrdNode node = getDirectoryRoot();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            node = node.searchSubNode(token);
        }
        return node;
    }

    private LrdProject getProject() {
        return repositoryBean.getProject();
    }
    
    /**
     * lrdRoot にノードを追加します。
     * @param lrdPath 追加するパス
     * @throws LrdException 
     */
    private void addNode(String lrdPath) throws LrdException {
        int index = lrdPath.lastIndexOf('/');
        if (index >= 0) {
            String baseDir = lrdPath.substring(0, index);
            StringTokenizer tokenizer = new StringTokenizer(baseDir, "/");
            
            // ROOTをnowに設定
            LrdNode now = getDirectoryRoot();
            
            // dir1/dir2/file1.lrd のようなパスを dir1/dir2 として / 区切りでループ
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                LrdNode orgNode = now; // now を避難
                
                // サブノードを探す
                now = now.searchSubNode(token);
                // サブノードが見つからなかった場合、ノードを新規作成
                if (now == null) {
                    String basePath = orgNode.getPath().getRelativePath();
                    if ("/".equals(basePath)) {
                        // ルート直下のみここを通る
                        now = new LrdNode(new LrdPath(getProject(), token), true);
                    } else {
                        now = new LrdNode(new LrdPath(getProject(), basePath + "/" + token), true);
                    }
                    orgNode.addSubNode(now);
                }
            }
            if (now != null) {
                // ファイルを追加
                now.addSubNode(new LrdNode(new LrdPath(getProject(), lrdPath), false));
            } else {
                throw new LrdException("ツリー内に追加先ディレクトリが見つかりません。" + lrdPath);
            }
        } else {
            lrdRoot.addSubNode(new LrdNode(new LrdPath(getProject(), lrdPath), false));
        }
    }


}
