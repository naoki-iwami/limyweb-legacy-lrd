/*
 * Created 2007/06/11
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
package org.limy.lrd;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.limy.common.resource.BaseDaoSupport;
import org.limy.lrd.common.bean.AdminLog;
import org.limy.lrd.common.bean.LrdCommentInfo;
import org.limy.lrd.common.bean.LrdHistory;

/**
 * Lrd関連DAO実装クラスです。
 * @author Naoki Iwami
 */
public class LrdDaoImpl extends BaseDaoSupport implements LrdDao {

    public boolean isEnable() {
        return true;
    }

    /**
     * Lrd履歴情報（RSS用）をINSERTします。
     * @param repositoryId リポジトリID
     * @param history Lrd履歴情報
     */
    public void insertLrdHistory(String repositoryId, LrdHistory history) {
        Map<String, Object> params = createParams();
        params.put("repositoryId", repositoryId);
        params.put("lrdPath", history.getLrdPath());
        params.put("htmlUrl", history.getHtmlUrl());
        params.put("commitTime", history.getCommitTime());
        insert("insertLrdHistory", params);
    }

    /**
     * Lrd履歴情報（RSS用）を取得します。
     * @param repositoryId リポジトリID
     * @param showCount 最大取得数
     * @return
     */
    public List<LrdHistory> getLrdHistories(String repositoryId, int showCount) {
        Map<String, Object> params = createParams();
        params.put("repositoryId", repositoryId);
        params.put("showCount", Integer.valueOf(showCount));
        return (List<LrdHistory>)selects("getLrdHistories", params);
    }

    /**
     * Lrdコメント情報をINSERTします。
     * @param info 
     */
    public void insertLrdComment(String repositoryId, LrdCommentInfo info) {
        Map<String, Object> params = createParams();
        params.put("repositoryId", repositoryId);
        params.put("lrdPath", info.getLrdPath());
        params.put("userName", info.getUserName());
        params.put("mainText", info.getMainText());
        params.put("ipAddress", info.getIpAddress());
        insert("insertLrdComment", params);
    }

    /**
     * Lrdファイルに対応するコメント一覧を取得します。
     * @param lrdPath Lrdファイルのパス
     * @return コメント一覧
     */
    public Collection<LrdCommentInfo> getLrdComments(String repositoryId, String lrdPath) {
        Map<String, Object> params = createParams();
        params.put("lrdPath", lrdPath);
        params.put("repositoryId", repositoryId);
        return (Collection<LrdCommentInfo>)selects("selectCommentInfos", params);
    }

    /**
     * 管理ログ情報をINSERTします。
     * @param adminLog 管理ログ情報
     */
    public void insertAdminLog(AdminLog adminLog) {
        Map<String, Object> params = createParams();
        params.put("userId", Integer.valueOf(adminLog.getUserId()));
        params.put("targetDate", adminLog.getTargetTimestamp());
        params.put("targetTime", adminLog.getTargetTimestamp());
        params.put("action", adminLog.getAction());
        params.put("note", adminLog.getNote());
        insert("insertAdminLog", params);
    }

}
