/*
 * Created 2007/08/15
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

import org.limy.common.LimyException;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.bean.LrdCommentInfo;

/**
 * データベース関連のモデルです。
 * @author Naoki Iwami
 */
public interface LrdDbModel {

    /**
     * LrdファイルをRSSのアイテムとして追加します。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @throws LrdException 共通例外
     */
    void addRss(String repositoryId, String lrdPath) throws LrdException;

    /**
     * コメントを投稿します。
     * @param repositoryId リポジトリID
     * @param info コメント情報
     * @throws LimyException 共通例外
     */
    void submitLrdComment(String repositoryId, LrdCommentInfo info) throws LimyException;
    
}
