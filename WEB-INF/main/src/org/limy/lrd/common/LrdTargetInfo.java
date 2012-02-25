/*
 * Created 2006/04/21
 * Copyright (C) 2003-2006  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limy-portal.
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



/**
 * Lrdの参照要素を表します。
 * @author Naoki Iwami
 */
public class LrdTargetInfo {
    
    // ------------------------ Fields
    
    /**
     * 参照文字列
     */
    private String text;
    
    /**
     * 参照先ターゲット
     */
    private LinkableTarget target;

    /**
     * 参照先ファイルでのターゲット番号
     */
    private String targetNo;

    // ------------------------ Constructors

    /**
     * LrdTargetInfoインスタンスを構築します。
     * @param text 参照文字列
     * @param targetFile 参照先ターゲット
     * @param targetNo 参照先ファイルでのターゲット番号
     */
    public LrdTargetInfo(String text, LinkableTarget targetFile, String targetNo) {
        super();
        this.text = text;
        this.target = targetFile;
        this.targetNo = targetNo;
    }

    // ------------------------ Getter/Setter Methods
    
    /**
     * 参照文字列を取得します。
     * @return 参照文字列
     */
    public String getText() {
        return text;
    }

    /**
     * 参照先ターゲットを取得します。
     * @return 参照先ターゲット
     */
    public LinkableTarget getTarget() {
        return target;
    }

    /**
     * 参照先ファイルでのターゲット番号を取得します。
     * @return 参照先ファイルでのターゲット番号
     */
    public String getTargetNo() {
        return targetNo;
    }

}
