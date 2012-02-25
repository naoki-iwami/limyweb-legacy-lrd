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

import java.util.Collection;
import java.util.Date;

import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.bean.LrdLightBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;

/**
 * リポジトリを扱うインターフェイスです。
 * @author Naoki Iwami
 */
public interface LrdRepository {

    LrdNode getDirectoryRoot() throws LrdException;
    
    /**
     * リポジトリからファイル内容を取得します（キャッシュ有）。
     * @param lrdPath Lrdパス
     * @return ファイル内容
     * @throws LrdException 共通例外
     */
    CommitFileInfo getRepositoryFile(String lrdPath) throws LrdException;

    /**
     * リポジトリから複数ファイルの内容を取得します（キャッシュ付）。
     * @param lrdPaths Lrdパス
     * @return ファイル内容
     * @throws LrdException 共通例外
     */
    CommitFileInfo[] getRepositoryFiles(
            String[] lrdPaths) throws LrdException;

    /**
     * リポジトリに複数ファイルをコミットします。
     * @param commitInfos コミット情報
     * @throws LrdException 共通例外
     */
    void commitMultiFile(CommitFileInfo[] commitInfos) throws LrdException;

    /**
     * リポジトリに複数ファイルを新規追加およびコミットします。
     * @param commitInfos コミット情報
     * @throws LrdException 共通例外
     */
    void addAndcommitMultiFile(CommitFileInfo[] commitInfos) throws LrdException;

    /**
     * リポジトリに空ファイルを追加します。
     * @param lrdPath Lrdパス
     * @throws LrdException 共通例外
     */
    void addFile(String lrdPath) throws LrdException;

    /**
     * リポジトリにディレクトリを追加します。
     * @param lrdPath Lrdパス
     * @throws LrdException 共通例外
     */
    void addDirectory(String lrdPath) throws LrdException;

    /**
     * リポジトリ内ファイルのタイムスタンプを返します。
     * @param lrdPath Lrdパス
     * @param authUser 認証ユーザ
     * @param authPass 認証パスワード
     * @return リポジトリ内ファイルのタイムスタンプ
     * @throws LrdException 共通例外
     */
    Date getTimestamp(
            String lrdPath, String authUser, String authPass)
            throws LrdException;

    /**
     * 全メニュー情報をHTML形式で取得します。
     * @param writer メニュー出力Writer
     * @return 全メニュー情報
     * @throws LrdException 共通例外
     */
    String getAllMenu(LrdWriter writer) throws LrdException;

    /**
     * 指定したディレクトリ直下にある全ファイルパスを返します（サブディレクトリは除く）。
     * @param repositoryUrl リポジトリURL
     * @return 全ファイルパス
     * @throws LrdException 共通例外
     */
    LrdPath[] getFilesFromDirectory(String repositoryUrl) throws LrdException;
    
    /**
     * 指定したパスにあるリソースがディレクトリかどうかを判定します。
     * @param project Lrdプロジェクト
     * @param repositoryUrl リポジトリURL
     * @return ディレクトリならばtrue
     * @throws LrdException 共通例外
     */
    boolean isDirectory(String repositoryUrl) throws LrdException;
    
    Collection<CommitFileInfo> getRecentUpdateFiles(int count) throws LrdException;


}
