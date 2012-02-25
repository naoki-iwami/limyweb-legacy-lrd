/*
 * Created 2007/01/29
 * Copyright (C) 2003-2007  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limy-web-lrd.
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
 * Lrdファイル更新履歴を表すBeanクラスです。
 * @author Naoki Iwami
 */
public class LrdHistory {

    // ------------------------ Fields

    /** ID */
    private int id;

    /** LrdPath */
    private String lrdPath;

    /** HTMLファイルのURL */
    private String htmlUrl;

    /** コミット時刻 */
    private Date commitTime;

    // ------------------------ Getter/Setter Methods

    /**
     * IDを取得します。
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * LrdPathを取得します。
     * @return LrdPath
     */
    public String getLrdPath() {
        return lrdPath;
    }

    /**
     * LrdPathを設定します。
     * @param lrdPath LrdPath
     */
    public void setLrdPath(String lrdPath) {
        this.lrdPath = lrdPath;
    }

    /**
     * HTMLファイルのURLを取得します。
     * @return HTMLファイルのURL
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * HTMLファイルのURLを設定します。
     * @param htmlUrl HTMLファイルのURL
     */
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
     * コミット時刻を取得します。
     * @return コミット時刻
     */
    public Date getCommitTime() {
        return (Date)commitTime.clone();
    }

    /**
     * コミット時刻を設定します。
     * @param commitTime コミット時刻
     */
    public void setCommitTime(Date commitTime) {
        this.commitTime = (Date)commitTime.clone();
    }

}
