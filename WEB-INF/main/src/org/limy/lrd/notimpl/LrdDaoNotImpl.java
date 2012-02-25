/*
 * Created 2007/08/04
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
package org.limy.lrd.notimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.limy.lrd.LrdDao;
import org.limy.lrd.common.bean.AdminLog;
import org.limy.lrd.common.bean.LrdCommentInfo;
import org.limy.lrd.common.bean.LrdHistory;

/**
 * @author Naoki Iwami
 */
public class LrdDaoNotImpl implements LrdDao {

    public boolean isEnable() {
        return false;
    }

    public Collection<LrdCommentInfo> getLrdComments(String repositoryName,
            String lrdPath) {
        return new ArrayList<LrdCommentInfo>(); // not support
    }

    public List<LrdHistory> getLrdHistories(String repositoryName, int showCount) {
        return new ArrayList<LrdHistory>(); // not support
    }

    public void insertAdminLog(AdminLog adminLog) {
        // not support
    }

    public void insertLrdComment(String repositoryName, LrdCommentInfo info) {
        // not support
    }

    public void insertLrdHistory(String repositoryName, LrdHistory history) {
        // not support
    }

}
