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
 * 識別子を持った要素を表します。
 * @author Naoki Iwami
 */
public interface IdentifyElement {
    
    /**
     * IDを取得します。
     * @return ID
     */
    String getId();

    /**
     * IDを設定します。
     * @param prefix プレフィックス文字列
     * @param number ナンバリング番号
     * @return ナンバリング後の番号
     */
    int setNumberingId(String prefix, int number);

}
