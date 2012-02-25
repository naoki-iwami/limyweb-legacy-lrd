/*
 * Created 2006/09/03
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
package org.limy.lrd.common.bean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.directwebremoting.annotations.DataTransferObject;
import org.limy.common.util.UrlUtils;
import org.limy.lrd.common.util.LrdUtils;

/**
 * DWR経由で返す値です。
 * @author Naoki Iwami
 */
@DataTransferObject
public class LrdDwrResult {
    
    // ------------------------ Fields

    /** エラー */
    private boolean error;
    
    /**
     * メイン文字列
     */
    private String mainString;
    
    /**
     * 警告文字列
     */
    private String alertString;

    // ------------------------ Constructors

    /**
     * LrdCompileResultインスタンスを構築します。
     */
    public LrdDwrResult() {
        super();
    }

    /**
     * LrdCompileResultインスタンスを構築します。
     * @param mainString メイン文字列
     */
    public LrdDwrResult(String mainString) {
        super();
        this.mainString = mainString;
    }
    
    // ------------------------ Public Methods

    /**
     * HTML内のリンクから管理用のリンクを作成してHTML内に追加します。
     * @param mainPath ファイルのパス
     */
    public void createAdminLinks(String mainPath) {
        Pattern pattern = Pattern.compile("<a href=\"([^\"]*)\">([^<]*)</a>", Pattern.MULTILINE);
        
        int bodyPos = mainString.indexOf("bodyColumn");
        
        Matcher matcher = pattern.matcher(mainString);
        String parent = UrlUtils.getParent(mainPath);
        
        StringBuilder buff = new StringBuilder();
        buff.append(mainString);
        
        int delayCount = 0;
        while (matcher.find()) {
            int pos = matcher.start();
            if (pos < bodyPos) {
                continue;
            }
            String href = matcher.group(1);
            if (href.indexOf("http:") >= 0) {
                continue;
            }
            
            String str;
            if (href.indexOf(".html#limy:") >= 0) {
                // 内部リンク
                str = "<a href=\"\">" + matcher.group(2) + "</a>";
            } else {
                // 外部リンク
                if (href.endsWith(".html")) {
                    // ファイル形式
                    href = LrdUtils.getLrdPath(href);
                } else {
                    // ディレクトリ形式
                    href = href + "/index.lrd";
                }
                str = "<a href=\"javascript:changeLocation('"
                    + UrlUtils.concatUrl(parent, href) + "')\">" + matcher.group(2) + "</a>";
            }
            buff.replace(pos + delayCount, pos + delayCount + matcher.group().length(), str);
            delayCount += str.length() - matcher.group().length();
        }
        mainString = buff.toString();
    }

    /**
     * エラーを取得します。
     * @return エラー
     */
    public boolean isError() {
        return error;
    }

    /**
     * エラーを設定します。
     * @param error エラー
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * メイン文字列を設定します。
     * @param mainString メイン文字列
     */
    public void setMainString(String mainString) {
        this.mainString = mainString;
    }

    /**
     * メイン文字列を取得します。
     * @return メイン文字列
     */
    public String getMainString() {
        return mainString;
    }

    /**
     * 警告文字列を設定します。
     * @param alertString 警告文字列
     */
    public void setAlertString(String alertString) {
        this.alertString = alertString;
    }

    /**
     * 警告文字列を取得します。
     * @return 警告文字列
     */
    public String getAlertString() {
        return alertString;
    }
    
}
