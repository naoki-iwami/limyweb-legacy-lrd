/*
 * Created 2007/06/16
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
package org.limy.lrd.common.bean;

import org.limy.common.ftp.FtpInfo;

/**
 * デプロイ先情報を表します。
 * @author Naoki Iwami
 */
public class LrdDeployInfo implements DeployInfo {

    /**
     * デプロイ種別
     * @author Naoki Iwami
     */
    public enum DeployType {
        /** ローカル */
        LOCAL,
        /** FTP */
        FTP;
    }
    
    // ------------------------ Fields
    
    /** デプロイ種別 */
    private final DeployType type;
    
    /** デプロイ先ID */
    private final String id;
    
    /** デプロイ先URL */
    private final String url;

    /** デプロイ先名称 */
    private String name;

    /** デプロイ先FTP接続情報 */
    private FtpInfo ftpInfo;

    // ------------------------ Constructors

    /**
     * LrdDeployInfo インスタンスを構築します。
     * @param type デプロイ種別
     * @param id デプロイ先ID
     * @param url デプロイ先URL（最後に"/"が付かない形式であること）
     */
    public LrdDeployInfo(DeployType type, String id, String url) {
        this.type = type;
        this.id = id;
        if (url.endsWith("/")) {
            throw new IllegalArgumentException("url [" + url + "] の最後に / が付いています。");
        }
        this.url = url;
    }
    
    // ------------------------ Getter/Setter Methods
    /**
     * デプロイ先URLを取得します。
     * @return デプロイ先URL
     */
    public String getDeployUrl() {
        return url;
    }

    /**
     * デプロイ種別を取得します。
     * @return デプロイ種別
     */
    public DeployType getType() {
        return type;
    }

    /**
     * デプロイ先IDを取得します。
     * @return デプロイ先ID
     */
    public String getId() {
        return id;
    }

    /**
     * デプロイ先名称を取得します。
     * @return デプロイ先名称
     */
    public String getName() {
        return name;
    }

    /**
     * デプロイ先名称を設定します。
     * @param name デプロイ先名称
     */
    public void setName(String name) {
        this.name = name;
    }

//    /**
//     * デプロイ先URLを取得します。
//     * @return デプロイ先URL
//     */
//    public String getUrl() {
//        return url;
//    }

    /**
     * デプロイ先FTP接続情報を取得します。
     * @return デプロイ先FTP接続情報
     */
    public FtpInfo getFtpInfo() {
        return ftpInfo;
    }

    /**
     * デプロイ先FTP接続情報を設定します。
     * @param ftpInfo デプロイ先FTP接続情報
     */
    public void setFtpInfo(FtpInfo ftpInfo) {
        this.ftpInfo = ftpInfo;
    }

}
