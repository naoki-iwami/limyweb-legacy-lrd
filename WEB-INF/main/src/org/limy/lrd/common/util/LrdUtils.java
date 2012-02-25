/*
 * Created 2007/08/17
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
package org.limy.lrd.common.util;

import java.io.File;
import java.io.IOException;

import org.limy.common.web.WebworkUtils;

/**
 * @author Naoki Iwami
 */
public final class LrdUtils {
    
    /**
     * private constructor
     */
    private LrdUtils() { }
    
    /**
     * HTMLファイルに対応するLrdファイルのパスを返します。
     * @param htmlPath HTMLファイルパス
     * @return Lrdファイルのパス
     */
    public static String getLrdPath(String htmlPath) {
        if (htmlPath.endsWith(".html")) {
            return htmlPath.substring(0, htmlPath.length() - 5) + ".lrd";
        }
        return htmlPath;
    }

    /**
     * Lrdファイルに対応するhtmlファイルのパスを返します。
     * @param lrdPath Lrdファイルパス
     * @return Lrdファイルのパス
     */
    public static String getHtmlPath(String lrdPath) {
        if (lrdPath.endsWith(".lrd")) {
            return lrdPath.substring(0, lrdPath.length() - 4) + ".html";
        }
        return lrdPath;
    }

    /**
     * ローカルコンテンツの基準URLを返します。
     * @param repositoryId リポジトリID
     * @return ローカルコンテンツの基準URL
     */
    public static String getLocalContentUrl(String repositoryId) {
        return WebworkUtils.getContextRootUrl() + "context/" + repositoryId;
    }

    /**
     * デフォルトのローカルコンテンツ基準ファイルを返します。
     * @param repositoryId リポジトリID
     * @return ローカルコンテンツ基準ファイル
     */
    public static File getDefaultLocalContentFile(String repositoryId) {
        return new File(WebworkUtils.getFile("context"), repositoryId);
    }
    
}
