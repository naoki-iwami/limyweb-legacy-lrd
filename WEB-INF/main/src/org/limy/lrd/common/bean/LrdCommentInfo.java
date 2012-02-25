/*
 * Created 2007/07/28
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

import java.util.Date;

import org.limy.common.util.Formatters;
import org.limy.common.util.HtmlUtils;

/**
 * @author Naoki Iwami
 */
public class LrdCommentInfo {
    
    // ------------------------ Fields

    /** Lrdパス */
    private String lrdPath;
    
    /** ユーザ名 */
    private String userName;
    
    /** 本文 */
    private String mainText;
    
    /** 送信時間 */
    private Date submitTime;

    /** 送信IP */
    private String ipAddress;

    // ------------------------ Public Methods

    public String getTime() {
        return Formatters.dateFormat(Formatters.D_VIEW_14E, submitTime);
    }
    
    public String getText() {
        return HtmlUtils.convertHtml(mainText);
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * Lrdパスを取得します。
     * @return Lrdパス
     */
    public String getLrdPath() {
        return lrdPath;
    }

    /**
     * Lrdパスを設定します。
     * @param lrdPath Lrdパス
     */
    public void setLrdPath(String lrdPath) {
        this.lrdPath = lrdPath;
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
     * 本文を取得します。
     * @return 本文
     */
    public String getMainText() {
        return mainText;
    }

    /**
     * 本文を設定します。
     * @param mainText 本文
     */
    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    /**
     * 送信時間を取得します。
     * @return 送信時間
     */
    public Date getSubmitTime() {
        return (Date)submitTime.clone();
    }

    /**
     * 送信時間を設定します。
     * @param submitTime 送信時間
     */
    public void setSubmitTime(Date submitTime) {
        this.submitTime = (Date)submitTime.clone();
    }

    /**
     * 送信IPを取得します。
     * @return 送信IP
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * 送信IPを設定します。
     * @param ipAddress 送信IP
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

}
