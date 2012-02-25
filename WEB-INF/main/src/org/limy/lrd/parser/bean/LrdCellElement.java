/*
 * Created 2007/08/14
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
package org.limy.lrd.parser.bean;

import org.limy.lrd.common.LrdTextElement;

/**
 * テーブル内セルを表します。
 * @author Naoki Iwami
 */
public class LrdCellElement {
    
    /** テキスト要素 */
    private final LrdTextElement textEl;
    
    /** ヘッダ */
    private final boolean header;

    // ------------------------ Constructors

    public LrdCellElement(LrdTextElement textEl, boolean header) {
        super();
        this.textEl = textEl;
        this.header = header;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * テキスト要素を取得します。
     * @return テキスト要素
     */
    public LrdTextElement getTextEl() {
        return textEl;
    }

    /**
     * ヘッダを取得します。
     * @return ヘッダ
     */
    public boolean isHeader() {
        return header;
    }

}
