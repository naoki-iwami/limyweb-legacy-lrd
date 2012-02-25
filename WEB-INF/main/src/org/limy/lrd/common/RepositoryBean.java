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
package org.limy.lrd.common;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.limy.common.repository.RepositoryInfo;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.common.bean.LrdDeployInfo.DeployType;

/**
 * リポジトリ情報を格納するBeanクラスです。
 * @author Naoki Iwami
 */
public class RepositoryBean implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -8735288542366194407L;

    /** 空Repository */
    private static final LrdRepository EMPTY_REPOSIROTY = new EmptyLrdRepository();

    /**
     * リポジトリ種別を表します。
     * @author Naoki Iwami
     */
    public static enum RepositoryType {
        /** ファイルリポジトリ */
        FILE,
        /** SVNリポジトリ */
        SVN;
    }

    /** リポジトリID */
    private String id;

    /** リポジトリ名 */
    private String name;
    
    /** リポジトリ種別 */
    private RepositoryType type;
    
    /** リポジトリURL */
    private String url;
    
    /** ユーザ名 */
    private String userName;
    
    /** パスワード */
    private String password;

    /** リポジトリ文字セット */
    private String repositoryCharset = "EUC-JP"; // リポジトリは通常LinuxにあるのでとりあえずEUC-JPを初期値に

    /** ビュー文字セット */
    private String viewCharset = "UTF-8";

    /** プロジェクト開始年 */
    private int startYear = Calendar.getInstance().get(Calendar.YEAR);

    /** 著作者 */
    private String author = "unknown";
    
    /** コメント有無フラグ */
    private boolean enableComment;

    /** ローカルコンテンツ基準ディレクトリ */
    private File localContentFile;

    /** デプロイ先一覧 */
    private Collection<LrdDeployInfo> deployInfos = new ArrayList<LrdDeployInfo>();
    
    /** このリポジトリに関連したRepositoryインターフェイス */
    private transient LrdRepository repository;
    
    /** Lrdプロジェクト */
    private transient LrdProject project;

    /** リポジトリ情報 */
    private transient RepositoryInfo repositoryInfo;

    // ------------------------ Public Methods

    /**
     * Lrdプロジェクトを設定します。
     * @param project Lrdプロジェクト
     */
    public void setProject(LrdProject project) {
        this.project = project;
    }

    public LrdProject getProject() {
        if (project == null) {
            project = new LrdProject(name);
//            project.setCharset(repositoryCharset);
            project.setRepositoryInfo(getRepositoryInfo());
        }
        return project;
    }

    /**
     * デプロイ先一覧を設定します。
     * @param deployInfos デプロイ先一覧
     */
    public void setDeployInfos(Collection<LrdDeployInfo> deployInfos) {
        this.deployInfos = deployInfos;
    }

    /**
     * このリポジトリに関連したRepositoryインターフェイスを取得します。
     * @return このリポジトリに関連したRepositoryインターフェイス
     */
    public LrdRepository getRepository() {
        if (repository == null) {
            return EMPTY_REPOSIROTY;
//            throw new IllegalStateException("準備中です…");
        }
        return repository;
    }
    
    public LrdDeployInfo getMainDeployInfo() {
        if (deployInfos.isEmpty()) {
            return null;
        }
        for (LrdDeployInfo info : deployInfos) {
            if (info.getType() != DeployType.LOCAL) {
                return info; // 最初に見つかったLOCAL以外のデプロイをMAINとする
            }
        }
        return deployInfos.iterator().next(); // 見つからなければLOCALをMAINに
    }
    
    public String getCheckedEnableComment() {
        return enableComment ? "CHECKED" : "";
    }

    // ------------------------ Getter/Setter Methods

    /**
     * このリポジトリに関連したRepositoryインターフェイスを設定します。
     * @param repository このリポジトリに関連したRepositoryインターフェイス
     */
    public void setRepository(LrdRepository repository) {
        this.repository = repository;
    }

    /**
     * リポジトリIDを取得します。
     * @return リポジトリID
     */
    public String getId() {
        return id;
    }

    /**
     * リポジトリIDを設定します。
     * @param id リポジトリID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * リポジトリ名を取得します。
     * @return リポジトリ名
     */
    public String getName() {
        return name;
    }

    /**
     * リポジトリ名を設定します。
     * @param name リポジトリ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * リポジトリ種別を取得します。
     * @return リポジトリ種別
     */
    public RepositoryType getType() {
        return type;
    }

    /**
     * リポジトリ種別を設定します。
     * @param type リポジトリ種別
     */
    public void setType(RepositoryType type) {
        this.type = type;
    }

    /**
     * リポジトリURLを取得します。
     * @return リポジトリURL
     */
    public String getUrl() {
        return url;
    }

    /**
     * リポジトリURLを設定します。
     * @param url リポジトリURL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * ユーザ名を取得します。
     * @return ユーザ名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * ユーザ名を設定します。
     * @param userName ユーザ名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * パスワードを取得します。
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定します。
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * コメント有無フラグを取得します。
     * @return コメント有無フラグ
     */
    public boolean isEnableComment() {
        return enableComment;
    }

    /**
     * コメント有無フラグを設定します。
     * @param enableComment コメント有無フラグ
     */
    public void setEnableComment(boolean enableComment) {
        this.enableComment = enableComment;
    }

    /**
     * デプロイ先一覧を取得します。
     * @return デプロイ先一覧
     */
    public Collection<LrdDeployInfo> getDeployInfos() {
        return deployInfos;
    }

    /**
     * デプロイ先を追加します。
     * @param deployInfo デプロイ先
     */
    public void addDeployInfo(LrdDeployInfo deployInfo) {
        deployInfos.add(deployInfo);
    }
    
    /**
     * リポジトリ文字セットを取得します。
     * @return リポジトリ文字セット
     */
    public String getRepositoryCharset() {
        return repositoryCharset;
    }

    /**
     * リポジトリ文字セットを設定します。
     * @param repositoryCharset リポジトリ文字セット
     */
    public void setRepositoryCharset(String repositoryCharset) {
        this.repositoryCharset = repositoryCharset;
    }

    /**
     * ローカルコンテンツ基準ディレクトリを設定します。
     * @param localContentFile ローカルコンテンツ基準ディレクトリ
     */
    public void setLocalContentFile(File localContentFile) {
        this.localContentFile = localContentFile;
    }

    /**
     * ビュー文字セットを取得します。
     * @return ビュー文字セット
     */
    public String getViewCharset() {
        return viewCharset;
    }

    /**
     * ビュー文字セットを設定します。
     * @param viewCharset ビュー文字セット
     */
    public void setViewCharset(String viewCharset) {
        this.viewCharset = viewCharset;
    }

    /**
     * プロジェクト開始年を取得します。
     * @return プロジェクト開始年
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * プロジェクト開始年を設定します。
     * @param startYear プロジェクト開始年
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     * 著作者を取得します。
     * @return 著作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 著作者を設定します。
     * @param author 著作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * リポジトリ情報を取得します。
     * @return リポジトリ情報
     */
    public RepositoryInfo getRepositoryInfo() {
        if (repositoryInfo == null) {
            repositoryInfo = new RepositoryInfo();
            repositoryInfo.setRepositoryUrl(url);
            repositoryInfo.setAuthUser(userName);
            repositoryInfo.setAuthPass(password);
        }
        return repositoryInfo;
    }

    /**
     * リポジトリ情報を設定します。
     * @param repositoryInfo リポジトリ情報
     */
    public void setRepositoryInfo(RepositoryInfo repositoryInfo) {
        this.repositoryInfo = repositoryInfo;
    }

    /**
     * ローカルコンテンツFileの基準パスを返します。
     * @return ローカルコンテンツFileの基準パス
     */
    public File getLocalContentFile() {
        return localContentFile;
    }
    
}
