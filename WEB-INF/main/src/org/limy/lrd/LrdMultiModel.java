/*
 * Created 2007/06/16
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

import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdDwrResult;
import org.limy.lrd.common.bean.LrdProject;

/**
 * Lrd関連のモデルインターフェイスです。
 * @author Naoki Iwami
 */
public interface LrdMultiModel {

    /**
     * リポジトリ上に空ファイルを作成します。
     * @param repositoryId リポジトリID
     * @param lrdPath 作成パス
     * @throws LrdException 共通例外
     */
    void createFile(String repositoryId, String lrdPath)
            throws LrdException;

    /**
     * 指定したLrdパスのコンテンツ内容を返します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdパス
     * @return Lrdコンテンツ内容
     * @throws LrdException 共通例外
     */
    LrdDwrResult changeLocation(String repositoryId, String lrdPath) throws LrdException;

    /**
     * Lrdファイルをコミットします。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @param contents Lrdコンテンツ内容
     * @return コンパイル結果
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    LrdDwrResult commit(String repositoryId, String lrdPath, String contents)
            throws IOException, LrdException;


    /**
     * リポジトリまたはキャッシュの内容からLrdBeanを作成して返します。
     * @param repositoryId リポジトリID
     * @param lrdPath Lrdパス
     * @return LedBean
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    LrdBean getBean(String repositoryId, String lrdPath) throws LrdException, IOException;

    /**
     * Lrdファイルをデプロイします。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @param deployId デプロイID
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    void deploy(String repositoryId, String lrdPath, String deployId)
            throws IOException, LrdException;

    /**
     * Lrdファイルを全デプロイします。
     * @param repositoryId リポジトリID
     * @param deployId デプロイID
     * @param session Webセッション
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    void deployAll(String repositoryId, String deployId,
            LimyWebSession session)
            throws IOException, LrdException;
    
    void deployRecentPage(String repositoryId, String deployId) throws LrdException, IOException;

    /**
     * リポジトリBeanを取得します。
     * @param repositoryId リポジトリID
     * @return リポジトリBean
     */
    RepositoryBean getRepositoryBean(String repositoryId);

    /**
     * Lrdプロジェクトを取得します。
     * @param repositoryId リポジトリID
     * @return Lrdプロジェクト
     */
    LrdProject getProject(String repositoryId);

    /**
     * Lrdリポジトリを取得します。
     * @param repositoryId リポジトリID
     * @return Lrdリポジトリ
     */
    LrdRepository getRepository(String repositoryId);

//    /**
//     * ローカルコンテンツの基準ファイルを返します。
//     * @param repositoryId リポジトリID
//     * @return ローカルコンテンツの基準ファイル
//     * @throws IOException I/O例外
//     */
//    File getLocalContentFile(String repositoryId) throws IOException;
  
}
