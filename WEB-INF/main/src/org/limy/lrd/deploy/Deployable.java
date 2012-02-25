/*
 * Created 2007/08/07
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
package org.limy.lrd.deploy;

import java.io.File;
import java.io.IOException;

import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;

/**
 * デプロイを担当します。
 * @author Naoki Iwami
 */
public interface Deployable {

    /**
     * 現在のWebセッションを取得します。
     * @return Webセッション
     */
    LimyWebSession getSession();
    
    /**
     * Lrdファイルをデプロイします。
     * @param lrdPath Lrdパス
     * @param outHtml 出力するHTMLの内容
     * @throws IOException I/O例外
     * @throws LrdException Lrd共通例外
     */
    void deployLrd(String lrdPath, String outHtml) throws IOException, LrdException;

    /**
     * 通常ファイルをデプロイします。
     * @param path ファイルパス
     * @param contents 出力するファイルの内容
     * @throws IOException I/O例外
     * @throws LrdException Lrd共通例外
     */
    void deployFile(String path, byte[] contents) throws IOException, LrdException;

    /**
     * 静的ファイルをデプロイします。
     * @param srcDir ソースディレクトリ
     * @param destDirName 出力先ディレクトリパス
     * @throws IOException I/O例外
     */
    void deployStaticFiles(File srcDir, String destDirName) throws IOException;

    /**
     * デプロイするファイルの合計数を設定します。
     * @param count デプロイするファイルの合計数
     */
    void setAllDeployCount(int count);

    /**
     * デプロイするファイルの合計数を返します。
     * @return デプロイするファイルの合計数
     */
    int getAllDeployCount();

    /**
     * デプロイしたときに呼び出します。
     * @param number デプロイしたファイル数
     */
    void addDeployCount(int number);

    /**
     * 現在デプロイが完了しているファイル数を返します。
     * @return 現在デプロイが完了しているファイル数
     */
    int getDeployCount();

    /**
     * リモートディレクトリを削除します。
     * @param dirName 削除するディレクトリパス
     * @throws IOException I/O例外
     */
    void deleteDir(String dirName) throws IOException;

}
