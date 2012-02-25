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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * 最近更新したLrdエントリをグルーピングして格納するクラスです。
 * @author Naoki Iwami
 */
public class RecentEntry {
    
    /** 更新日付 */
    private Date date;
    
    /** 更新したLrd一覧 */
    private Collection<RecentEntryChild> children;

    public RecentEntry() {
        children = new ArrayList<RecentEntryChild>();
    }
    
    public void addSubEntry(RecentEntryChild child) {
        children.add(child);
    }
    
    public String getDateStr() {
        return new SimpleDateFormat("yyyy/MM/dd").format(date);
    }
    
    /**
     * 更新日付を設定します。
     * @param date 更新日付
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 更新したLrd一覧を取得します。
     * @return 更新したLrd一覧
     */
    public Collection<RecentEntryChild> getChildren() {
        return children;
    }
    
}
