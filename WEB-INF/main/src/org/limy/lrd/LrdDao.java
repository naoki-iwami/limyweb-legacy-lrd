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

import org.limy.lrd.common.bean.AdminLog;
import org.limy.lrd.common.bean.LrdCommentInfo;
import org.limy.lrd.common.bean.LrdHistory;

/**
 * Lrd関連DAOインターフェイスです。
 * @author Naoki Iwami
 */
public interface LrdDao {

    /**
     * このインターフェイスが有効かどうかを返します。
     * @return 有効ならばtrue
     */
    boolean isEnable();
    
    /**
     * Lrd履歴情報（RSS用）をINSERTします。
     * @param repositoryId リポジトリID
     * @param history Lrd履歴情報
     */
    void insertLrdHistory(String repositoryId, LrdHistory history);

    /**
     * Lrd履歴情報（RSS用）を取得します。
     * @param repositoryId リポジトリID
     * @param showCount 最大取得数
     * @return
     */
    List<LrdHistory> getLrdHistories(String repositoryId, int showCount);

    /**
     * Lrdコメント情報をINSERTします。
     * @param repositoryId リポジトリID
     * @param info 
     */
    void insertLrdComment(String repositoryId, LrdCommentInfo info);

    /**
     * Lrdファイルに対応するコメント一覧を取得します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdファイルのパス
     * @return コメント一覧
     */
    Collection<LrdCommentInfo> getLrdComments(String repositoryId, String lrdPath);

    /**
     * 管理ログ情報をINSERTします。
     * @param adminLog 管理ログ情報
     */
    void insertAdminLog(AdminLog adminLog);

}
