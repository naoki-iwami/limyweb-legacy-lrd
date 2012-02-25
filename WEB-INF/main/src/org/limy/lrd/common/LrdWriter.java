/*
 * Created 2007/06/15
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
import java.io.Writer;
import java.util.Collection;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.limy.lrd.bean.RecentEntry;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;

/**
 * Lrd出力インターフェイスです。
 * @author Naoki Iwami
 */
public interface LrdWriter {
    
    /**
     * Lrdの内容をHTML形式で出力します。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @param extInfos 追加参照情報
     * @param menuRoot メニューツリー情報
     * @param contextOrg コンテキスト情報
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    void writeHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Writer out, Collection<LrdTargetInfo> extInfos,
            LrdNode menuRoot, Context contextOrg)
            throws IOException, LrdException;

    /**
     * 任意のテンプレートを使ってLrdの内容をHTML形式で出力します。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @param menuRoot メニューツリー情報
     * @param contextOrg コンテキスト情報
     * @param template 任意のテンプレート
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    void writeHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Writer out,
            LrdNode menuRoot, Template template)
            throws IOException, LrdException;

    /**
     * Lrdの内容をシンプルなHTML形式で出力します。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @param extInfos 追加参照情報
     * @param menuRoot メニューツリー情報
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    void writeSimpleHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Writer out,
            Collection<LrdTargetInfo> extInfos,
            LrdNode menuRoot)
            throws IOException, LrdException;
    
    /**
     * 全メニューをHTMLに出力します。
     * @param repositoryBean リポジトリBean
     * @param out 出力先
     * @param menuRoot メニューツリー情報
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    void writeAllMenu(
            RepositoryBean repositoryBean,
            Writer out, LrdNode menuRoot) throws IOException, LrdException;

    void writeRecentPage(
            RepositoryBean repositoryBean, LrdDeployInfo info,
            Writer out, Collection<RecentEntry> recentUpdates) throws IOException, LrdException;
    
    /**
     * 参照先情報を付加してHTML形式の出力を返します。
     * @param bean LrdBean
     * @param extInfos 参照先情報
     * @return HTML形式の出力
     * @throws IOException I/O例外
     */
    String createHtmlString(LrdBean bean, Collection<LrdTargetInfo> extInfos)
            throws IOException;
    
    /**
     * 対象Bean用のメニューHTMLを生成して返します。
     * @param repositoryBean リポジトリBean
     * @param bean 対象Bean
     * @param menuNode メニューノード
     * @return メニューHTML
     * @throws IOException 
     * @throws LrdException 共通例外
     */
    String getMenuContents(
            RepositoryBean repositoryBean,
            LrdBean bean, LrdNode menuNode) throws IOException, LrdException;

}
