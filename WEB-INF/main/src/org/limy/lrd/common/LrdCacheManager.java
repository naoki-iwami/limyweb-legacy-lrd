/*
 * Created 2007/07/08
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
package org.limy.lrd.common;

import java.io.IOException;

import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;

/**
 * Lrd用キャッシュ管理インターフェイスです。
 * @author Naoki Iwami
 */
public interface LrdCacheManager {
    
    /**
     * LrdBeanを生成して返します。
     * @param repositoryName リポジトリ名
     * @param lrdPath Lrdパス
     * @param contents コンテンツ内容
     * @param saveCache 生成した結果をキャッシュに格納するかどうか
     * @return LrdBean
     * @throws IOException I/O例外
     */
    LrdBean createBean(
            String repositoryName,
            String lrdPath, String contents, boolean saveCache) throws IOException;

    /**
     * LrdBeanを返します（キャッシュ使用）。
     * <p>
     * キャッシュに存在しなかった場合は作成して返します。
     * </p>
     * @param repositoryId リポジトリID
     * @param info Lrd内容
     * @return LrdBean
     * @throws IOException I/O例外
     */
    LrdBean getBean(String repositoryId,
            CommitFileInfo info) throws IOException;
    
    /**
     * ダミー用のLrdBeanを生成して返します。
     * @param repositoryId リポジトリID
     * @param contents コンテンツ内容
     * @return LrdBean
     * @throws IOException I/O例外
     */
    LrdBean createDummyBean(
            String repositoryId, String contents) throws IOException;
        
    /**
     * ディレクトリのインデックスファイルタイトルを返します（キャッシュ有）。
     * @param repositoryId リポジトリID
     * @param path ディレクトリのパス名
     * @return タイトル
     * @throws IOException I/O例外
     */
    String getIndexLrdTitle(String repositoryId,
            LrdPath path) throws IOException;

    void clearLrdCache(String repositoryId, String lrdPath) throws IOException;

    void clearMenuTreeCache(String repositoryId);

    LrdNode getMenuTree(String repositoryId,
            LrdBean bean) throws LrdException, IOException;

    String getMenuContents(String repositoryId,
            LrdBean bean) throws LrdException, IOException;

}
