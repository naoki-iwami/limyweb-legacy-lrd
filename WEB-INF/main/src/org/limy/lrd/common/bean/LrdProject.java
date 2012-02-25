/*
 * Created 2006/04/25
 * Copyright (C) 2003-2006  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limy-portal.
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
package org.limy.lrd.common.bean;

import org.limy.common.ftp.FtpInfo;
import org.limy.common.repository.RepositoryInfo;

/**
 * Lrdプロジェクトを表します。
 * @author Naoki Iwami
 */
public class LrdProject {
    
    // ------------------------ Fields

    /**
     * プロジェクトID
     */
    private String id;
    
//    /**
//     * プロジェクトの文字セット
//     */
//    private String charset;
    
    /**
     * リポジトリ情報
     */
    private RepositoryInfo repositoryInfo;
    
    /**
     * FTPデプロイ情報
     */
    private FtpInfo deployInfo;
    
//    /**
//     * ルートディレクトリ名
//     */
//    private String rootDirName;

    // ------------------------ Constructors

    /**
     * LrdProjectインスタンスを構築します。
     * @param projectId
     */
    public LrdProject(String projectId) {
        this.id = projectId;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * プロジェクトIDを取得します。
     * @return プロジェクトID
     */
    public String getId() {
        return id;
    }

    /**
     * プロジェクトIDを設定します。
     * @param id プロジェクトID
     */
    public void setId(String id) {
        this.id = id;
    }
//
//    /**
//     * プロジェクトの文字セットを取得します。
//     * @return プロジェクトの文字セット
//     */
//    public String getCharset() {
//        return charset;
//    }
//
//    /**
//     * プロジェクトの文字セットを設定します。
//     * @param charset プロジェクトの文字セット
//     */
//    public void setCharset(String charset) {
//        this.charset = charset;
//    }

    /**
     * リポジトリ情報を取得します。
     * @return リポジトリ情報
     */
    public RepositoryInfo getRepositoryInfo() {
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
     * FTPデプロイ情報を取得します。
     * @return FTPデプロイ情報
     */
    public FtpInfo getDeployInfo() {
        return deployInfo;
    }

    /**
     * FTPデプロイ情報を設定します。
     * @param deployInfo FTPデプロイ情報
     */
    public void setDeployInfo(FtpInfo deployInfo) {
        this.deployInfo = deployInfo;
    }

//    /**
//     * ルートディレクトリ名を取得します。
//     * @return ルートディレクトリ名
//     */
//    public String getRootDirName() {
//        return rootDirName;
//    }
//
//    /**
//     * ルートディレクトリ名を設定します。
//     * @param rootDirName ルートディレクトリ名
//     */
//    public void setRootDirName(String rootDirName) {
//        this.rootDirName = rootDirName;
//    }

}
