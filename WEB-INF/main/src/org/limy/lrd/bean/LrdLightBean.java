/*
 * Created 2012/02/19
 * Copyright (C) 2003-2012  Naoki Iwami (naoki@limy.org)
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
package org.limy.lrd.bean;

import java.util.Date;

/**
 * 簡易LrdBeanクラスです。
 * @author Naoki Iwami
 */
public class LrdLightBean {
    
    private Date lastUpdateDate;
    
    private String title;
    
    private String lrdPath;

    /**
     * lastUpdateDateを取得します。
     * @return lastUpdateDate
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * lastUpdateDateを設定します。
     * @param lastUpdateDate lastUpdateDate
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * titleを取得します。
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * titleを設定します。
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * lrdPathを取得します。
     * @return lrdPath
     */
    public String getLrdPath() {
        return lrdPath;
    }

    /**
     * lrdPathを設定します。
     * @param lrdPath lrdPath
     */
    public void setLrdPath(String lrdPath) {
        this.lrdPath = lrdPath;
    }

}
