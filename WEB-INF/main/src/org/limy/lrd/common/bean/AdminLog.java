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
package org.limy.lrd.common.bean;

import java.util.Date;

/**
 * @author Naoki Iwami
 */
public class AdminLog {

    // ------------------------ Fields

    /** ユーザID */
    private int userId;
    
    /** 対象タイムスタンプ */
    private Date targetTimestamp;
    
    /** アクション名 */
    private String action;
    
    /** メモ */
    private String note;

    // ------------------------ Getter/Setter Methods

    /**
     * ユーザIDを取得します。
     * @return ユーザID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * ユーザIDを設定します。
     * @param userId ユーザID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 対象タイムスタンプを取得します。
     * @return 対象タイムスタンプ
     */
    public Date getTargetTimestamp() {
        return (Date)targetTimestamp.clone();
    }

    /**
     * 対象タイムスタンプを設定します。
     * @param targetTimestamp 対象タイムスタンプ
     */
    public void setTargetTimestamp(Date targetTimestamp) {
        this.targetTimestamp = (Date)targetTimestamp.clone();
    }

    /**
     * アクション名を取得します。
     * @return アクション名
     */
    public String getAction() {
        return action;
    }

    /**
     * アクション名を設定します。
     * @param action アクション名
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * メモを取得します。
     * @return メモ
     */
    public String getNote() {
        return note;
    }

    /**
     * メモを設定します。
     * @param note メモ
     */
    public void setNote(String note) {
        this.note = note;
    }

}
