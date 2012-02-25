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

import java.util.ArrayList;
import java.util.Collection;

import org.limy.lrd.common.LrdTextElement;

/**
 * テーブル内の1行を表す要素です。
 * @author Naoki Iwami
 */
public class LrdRowElement {
    
    /** 列ごとのセル要素 */
    private Collection<LrdCellElement> cells = new ArrayList<LrdCellElement>();
    
    /**
     * 列数を返します。
     * @return 列数
     */
    public int getColumnSize() {
        return cells.size();
    }
    
    /**
     * セルを追加します。
     * @param textEl テキスト要素
     * @param header ヘッダならばtrue
     */
    public void addText(LrdTextElement textEl, boolean header) {
        cells.add(new LrdCellElement(textEl, header));
    }
    
    /**
     * セル一覧を返します。
     * @return セル一覧
     */
    public Iterable<LrdCellElement> getCells() {
        return cells;
    }

}
