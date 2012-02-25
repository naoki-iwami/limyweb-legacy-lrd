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

import java.io.IOException;
import java.util.Collection;

import org.limy.lrd.bean.RecentEntry;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.bean.LrdDwrResult;
import org.limy.lrd.common.bean.LrdNode;

/**
 * コンパイル関連のモデルです。
 * @author Naoki Iwami
 */
public interface LrdCompileModel {

    /**
     * LrdファイルをコンパイルしてHTML形式で返します。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @param contents Lrdコンテンツ内容
     * @return HTML形式文字列
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    LrdDwrResult compile(String repositoryId, String lrdPath, String contents)
            throws IOException, LrdException;

    /**
     * 任意のテンプレートを使ってLrdファイルをコンパイルしてHTML形式で返します。
     * @param repositoryId リポジトリID
     * @param contents 任意のLrdコンテンツ内容
     * @param templateString 任意のテンプレート文字列
     * @return HTML形式文字列
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    LrdDwrResult compileWithTemplate(String repositoryId,  String contents,
            String templateString) throws IOException, LrdException;


    /**
     * LrdBeanに関連するメニュー要素ツリーを作成して返します。
     * @param repositoryId リポジトリID
     * @param bean LrdBean
     * @return メニュー要素ツリー
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    LrdNode createMenuTree(String repositoryId, LrdBean bean)
            throws LrdException, IOException;

    /**
     * 全メニューをHTML形式で取得します。
     * @param repositoryId リポジトリID
     * @return 全メニュー
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    String getAllMenu(String repositoryId) throws LrdException, IOException;
    
    String getRecentEntryHtml(String repositoryId, String deployId)
            throws LrdException, IOException;

}
