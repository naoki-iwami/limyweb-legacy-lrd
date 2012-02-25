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
package org.limy.lrd.admin;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.web.WebworkUtils;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.RepositoryBean.RepositoryType;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdDeployInfo.DeployType;
import org.limy.lrd.common.util.LrdUtils;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNException;

/**
 * リポジトリ新規追加アクションクラスです。
 * @author Naoki Iwami
 */
public class AddRepositoryAction extends AbstractAdminAction {

    /** logger */
    private static final Log LOG = LogFactory.getLog(AddRepositoryAction.class);

    /** デフォルトのデプロイID */
    private static final String DEFAULT_DEPLOY_ID = "LOCAL";
    
    // ------------------------ Fields

    /** リポジトリID */
    private String repositoryId;

    /** リポジトリ名 */
    private String repositoryName;
    
    /** リポジトリ種別 */
    private int repositoryType;

    /** リポジトリURL種別 */
    private int repositoryUrlType;

    /** リポジトリURL（ファイル） */
    private String repositoryUrl;

    /** リポジトリURL（SVN） */
    private String repositorySvnUrl;

    /** SVNユーザ名 */
    private String repositorySvnUser;

    /** SVNパスワード */
    private String repositorySvnPassword;

    /** ローカルコンテキストディレクトリ */
    private String localContextDir;

    // ------------------------ Override Methods

    @Override
    protected String doExecuteAdmin() {
        
        RepositoryBean bean = new RepositoryBean();
        bean.setId(repositoryId);
        bean.setName(repositoryName);
        
        if (StringUtils.isEmpty(localContextDir)) {
            bean.setLocalContentFile(
                    LrdUtils.getDefaultLocalContentFile(repositoryId));
        } else {
            bean.setLocalContentFile(new File(localContextDir));
        }
        
        if (repositoryType == 1) { // SVN
            bean.setType(RepositoryType.SVN);
            if (!repositorySvnUrl.endsWith("/")) {
                repositorySvnUrl += "/";
            }
            bean.setUrl(repositorySvnUrl);
            bean.setUserName(repositorySvnUser);
            bean.setPassword(repositorySvnPassword);
        } else { // FILE
            bean.setType(RepositoryType.FILE);
            if (repositoryUrlType == 0) { // デフォルト
                bean.setUrl(new File(WebworkUtils.getFile("repository"), repositoryId)
                        .getAbsolutePath());
            } else { // カスタマイズ
                bean.setUrl(repositoryUrl);
            }
        }
        
        getconfigRootBean().addRepository(bean);
        getRepositoryCreator().refreshRepositoryBeans();
        
        try {
            createEmpryContents(bean);
        } catch (LrdException e) {
            LOG.warn(e.getMessage(), e);
            return ERROR;
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
            return ERROR;
        }
       
        updateConfigRootBean();
        return SUCCESS;
    }
    
    // ------------------------ Web Getter Methods

    /**
     * リポジトリIDを取得します。
     * @return リポジトリID
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * リポジトリIDを設定します。
     * @param repositoryId リポジトリID
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * リポジトリ名を取得します。
     * @return リポジトリ名
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * リポジトリ名を設定します。
     * @param repositoryName リポジトリ名
     */
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * リポジトリ種別を取得します。
     * @return リポジトリ種別
     */
    public int getRepositoryType() {
        return repositoryType;
    }

    /**
     * リポジトリ種別を設定します。
     * @param repositoryType リポジトリ種別
     */
    public void setRepositoryType(int repositoryType) {
        this.repositoryType = repositoryType;
    }

    /**
     * リポジトリURL種別を取得します。
     * @return リポジトリURL種別
     */
    public int getRepositoryUrlType() {
        return repositoryUrlType;
    }

    /**
     * リポジトリURL種別を設定します。
     * @param repositoryUrlType リポジトリURL種別
     */
    public void setRepositoryUrlType(int repositoryUrlType) {
        this.repositoryUrlType = repositoryUrlType;
    }

    /**
     * リポジトリURL（ファイル）を取得します。
     * @return リポジトリURL（ファイル）
     */
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    /**
     * リポジトリURL（ファイル）を設定します。
     * @param repositoryUrl リポジトリURL（ファイル）
     */
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    /**
     * リポジトリURL（SVN）を取得します。
     * @return リポジトリURL（SVN）
     */
    public String getRepositorySvnUrl() {
        return repositorySvnUrl;
    }

    /**
     * リポジトリURL（SVN）を設定します。
     * @param repositorySvnUrl リポジトリURL（SVN）
     */
    public void setRepositorySvnUrl(String repositorySvnUrl) {
        this.repositorySvnUrl = repositorySvnUrl;
    }

    /**
     * SVNユーザ名を取得します。
     * @return SVNユーザ名
     */
    public String getRepositorySvnUser() {
        return repositorySvnUser;
    }

    /**
     * SVNユーザ名を設定します。
     * @param repositorySvnUser SVNユーザ名
     */
    public void setRepositorySvnUser(String repositorySvnUser) {
        this.repositorySvnUser = repositorySvnUser;
    }

    /**
     * SVNパスワードを取得します。
     * @return SVNパスワード
     */
    public String getRepositorySvnPassword() {
        return repositorySvnPassword;
    }

    /**
     * SVNパスワードを設定します。
     * @param repositorySvnPassword SVNパスワード
     */
    public void setRepositorySvnPassword(String repositorySvnPassword) {
        this.repositorySvnPassword = repositorySvnPassword;
    }

    /**
     * ローカルコンテキストディレクトリを取得します。
     * @return ローカルコンテキストディレクトリ
     */
    public String getLocalContextDir() {
        return localContextDir;
    }

    /**
     * ローカルコンテキストディレクトリを設定します。
     * @param localContextDir ローカルコンテキストディレクトリ
     */
    public void setLocalContextDir(String localContextDir) {
        this.localContextDir = localContextDir;
    }

    // ------------------------ Private Methods

    private void createEmpryContents(RepositoryBean bean) throws LrdException, IOException {
        
        if (bean.getType() == RepositoryType.FILE) {
            initLocalRepository(bean);
        }
        if (bean.getType() == RepositoryType.SVN) {
            initSvnRepository(bean);
        }
        copyDefaultTemplateFiles(bean);
        saveDefaultTemplateFiles(bean);
        
        // リポジトリRootの作成と初期ファイルの登録
        createInitFiles(bean);

        // デフォルトでローカルデプロイ先を設定 
        LrdDeployInfo deployInfo = new LrdDeployInfo(DeployType.LOCAL,
                DEFAULT_DEPLOY_ID, LrdUtils.getLocalContentUrl(repositoryId));
        deployInfo.setName(DEFAULT_DEPLOY_ID);
        bean.addDeployInfo(deployInfo);
    }

    /**
     * ローカルリポジトリを初期化します。
     * @param bean リポジトリBaan
     * @throws LrdException 
     * @throws IOException I/O例外
     */
    private void initLocalRepository(RepositoryBean bean)
            throws LrdException, IOException {
        
        File baseDir = new File(bean.getUrl());
        if (!baseDir.exists()) {
            // リポジトリルートが存在しなかったらデフォルトリポジトリを作成
            baseDir.mkdirs();
//            createInitFiles(bean);
        }
    }

    /**
     * SVNリポジトリを初期化します。
     * @param bean リポジトリBaan
     * @throws LrdException 
     * @throws IOException 
     */
    private void initSvnRepository(RepositoryBean bean) throws LrdException, IOException {
        LrdRepository repository = bean.getRepository();
        try {
            // ディレクトリルートを取得して存在を確認
            repository.getDirectoryRoot();
        } catch (LrdException e) {
            if (e.getCause() instanceof SVNException) {
                // リポジトリが存在しない
                SVNException se = (SVNException)e.getCause();
                SVNErrorCode errorCode = se.getErrorMessage().getErrorCode();
                if (SVNErrorCode.FS_NOT_FOUND == errorCode) {
                    // リポジトリ自体はあるが、パスが存在しない
                    createEmptySvnRepository(bean);
                    return;
                }
                if (SVNErrorCode.RA_SVN_REPOS_NOT_FOUND == errorCode) {
                    // リポジトリ自体が存在しない
                    // TODO svnadmin バイナリを利用してリポジトリの新規作成をする必要がある。現在は未対応
                }
                // TODO 認証失敗の場合も考慮する必要あり
                throw e;
            }
        }
    }

    /**
     * SVNリポジトリを空で作成します。
     * @param bean リポジトリBaan
     * @throws LrdException 
     */
    private void createEmptySvnRepository(RepositoryBean bean) throws LrdException {
        LrdRepository repository = bean.getRepository();
        repository.addDirectory("");
    }

    /**
     * 各種テンプレートファイルをローカルデプロイ先にコピーします。
     * @param bean 
     * @throws LrdException 
     * @throws IOException 
     */
    private void copyDefaultTemplateFiles(RepositoryBean bean) throws LrdException, IOException {
        
        File baseDir = bean.getLocalContentFile();

        // リポジトリ上にテンプレートファイルが存在したらそれを使う
        LrdRepository repository = bean.getRepository();
        LrdNode root = repository.getDirectoryRoot();
        LrdNode node = root.searchSubNode(".template");
        if (node != null) {
            Collection<String> lrdPaths = new ArrayList<String>();
            for (LrdNode child : node.getSubNodes()) {
                for (LrdNode file : child.getSubNodes()) {
                    lrdPaths.add(file.getPath().getRelativePath());
                }
            }
            CommitFileInfo[] files = repository.getRepositoryFiles(
                    lrdPaths.toArray(new String[lrdPaths.size()]));
            for (CommitFileInfo file : files) {
                FileUtils.writeByteArrayToFile(
                        new File(baseDir, file.getPath()),
                        file.getContents());
            }
            return;
        }

        for (File file : WebworkUtils.getFile("template/vm").listFiles(
                (FileFilter)FileFileFilter.FILE)) {
            FileUtils.copyFile(file,
                    new File(baseDir, ".template/vm/" + file.getName())
            );
        }
        for (File file : WebworkUtils.getFile("template/style").listFiles(
                (FileFilter)FileFileFilter.FILE)) {

            FileUtils.copyFile(file,
                    new File(baseDir, ".template/style/" + file.getName())
            );
        }
    }

    /**
     * 各種テンプレートファイルをリポジトリにコミットします。
     * @param bean 
     * @throws IOException 
     */
    private void saveDefaultTemplateFiles(RepositoryBean bean)
            throws IOException {

        LrdRepository repository = bean.getRepository();
        
        Collection<CommitFileInfo> commitInfos = new ArrayList<CommitFileInfo>();
        for (File file : WebworkUtils.getFile("template/vm").listFiles(
                (FileFilter)FileFileFilter.FILE)) {

            commitInfos.add(new CommitFileInfo(".template/vm/" + file.getName(),
                    FileUtils.readFileToByteArray(file), null));
        }
        for (File file : WebworkUtils.getFile("template/style").listFiles(
                (FileFilter)FileFileFilter.FILE)) {

            commitInfos.add(new CommitFileInfo(".template/style/" + file.getName(),
                    FileUtils.readFileToByteArray(file), null));
        }

        try {
            repository.addAndcommitMultiFile(
                    commitInfos.toArray(new CommitFileInfo[commitInfos.size()]));
        } catch (LrdException e) {
            // テンプレートのコミットは失敗しても処理を続ける
            LOG.error(e.getMessage(), e);
        }
    }
    
    /**
     * 初期ファイルをリポジトリに登録します。
     * @param bean 
     * @throws LrdException 
     * @throws IOException I/O例外
     */
    private void createInitFiles(RepositoryBean bean)
            throws LrdException, IOException {
        
        LrdNode root = bean.getRepository().getDirectoryRoot();
        if (root.searchSubNode("index.lrd") == null) {
            // index.lrd を作成してローカルにデプロイ
            getModel().createFile(repositoryId, "index.lrd");
            String contents = IOUtils.toString(
                    new FileInputStream(WebworkUtils.getFile("WEB-INF/cnf/empty.lrd")));
            getModel().commit(repositoryId, "index.lrd", contents);
//            new LrdDwrExt().deployFull(bean.getId(), DEFAULT_DEPLOY_ID);
        }
    }

}
