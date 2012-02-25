/*
 * Created 2007/08/02
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

import org.limy.common.ftp.FtpInfo;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdDeployInfo.DeployType;

/**
 * デプロイ先を追加するアクションクラスです。
 * @author Naoki Iwami
 */
public class AddDeployInfoAction extends AbstractAdminAction {
    
    // ------------------------ Fields

    /** リポジトリID */
    private String repositoryId;

    /** デプロイID */
    private String deployId;

    /** HTTP URL */
    private String httpUrl;

    /** FTPサーバ名 */
    private String ftpServer;

    /** FTPルートファイルパス */
    private String ftpPath;

    /** FTPユーザ名 */
    private String ftpUser;

    /** FTPパスワード */
    private String ftpPassword;

    // ------------------------ Implement Methods

    @Override
    protected String doExecuteAdmin() {
        
        RepositoryBean bean = getModel().getRepositoryBean(repositoryId);
        
        if (httpUrl.endsWith("/")) {
            httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
        }

        LrdDeployInfo deployInfo = new LrdDeployInfo(DeployType.FTP, deployId, httpUrl);
        deployInfo.setName(deployId);
        FtpInfo ftpInfo = new FtpInfo();
        ftpInfo.setServerAddress(ftpServer);
        ftpInfo.setPath(ftpPath);
        ftpInfo.setUserName(ftpUser);
        ftpInfo.setPassword(ftpPassword);
        deployInfo.setFtpInfo(ftpInfo);
        bean.addDeployInfo(deployInfo);
        updateConfigRootBean();
        
        return SUCCESS;
    }

    // ------------------------ Getter/Setter Methods

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
     * デプロイIDを取得します。
     * @return デプロイID
     */
    public String getDeployId() {
        return deployId;
    }

    /**
     * デプロイIDを設定します。
     * @param deployId デプロイID
     */
    public void setDeployId(String deployId) {
        this.deployId = deployId;
    }

    /**
     * HTTP URLを取得します。
     * @return HTTP URL
     */
    public String getHttpUrl() {
        return httpUrl;
    }

    /**
     * HTTP URLを設定します。
     * @param httpUrl HTTP URL
     */
    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    /**
     * FTPサーバ名を取得します。
     * @return FTPサーバ名
     */
    public String getFtpServer() {
        return ftpServer;
    }

    /**
     * FTPサーバ名を設定します。
     * @param ftpServer FTPサーバ名
     */
    public void setFtpServer(String ftpServer) {
        this.ftpServer = ftpServer;
    }

    /**
     * FTPルートファイルパスを取得します。
     * @return FTPルートファイルパス
     */
    public String getFtpPath() {
        return ftpPath;
    }

    /**
     * FTPルートファイルパスを設定します。
     * @param ftpPath FTPルートファイルパス
     */
    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    /**
     * FTPユーザ名を取得します。
     * @return FTPユーザ名
     */
    public String getFtpUser() {
        return ftpUser;
    }

    /**
     * FTPユーザ名を設定します。
     * @param ftpUser FTPユーザ名
     */
    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    /**
     * FTPパスワードを取得します。
     * @return FTPパスワード
     */
    public String getFtpPassword() {
        return ftpPassword;
    }

    /**
     * FTPパスワードを設定します。
     * @param ftpPassword FTPパスワード
     */
    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

}
