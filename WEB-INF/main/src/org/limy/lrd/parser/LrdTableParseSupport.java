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
package org.limy.lrd.parser;

import java.io.IOException;

import org.limy.lrd.common.LrdTextElement;
import org.limy.lrd.common.LrdTextElement.Type;
import org.limy.lrd.parser.bean.LrdRowElement;
import org.limy.lrd.parser.bean.LrdTableElement;

/**
 * @author Naoki Iwami
 */
/* package */ class LrdTableParseSupport implements LrdParseSupport {

    public Parsed parse(LrdParserCore core, String line) throws IOException {
        
        if (line.length() > 0 && line.charAt(0) == '|') {
            
            boolean header;
            String[] cells;
            if (line.endsWith("|h")) {
                header = true;
                cells = line.substring(1, line.length() - 1).split("\\|");
            } else if (line.endsWith("|")) {
                header = false;
                cells = line.substring(1).split("\\|");
            } else {
                return Parsed.NO;
            }

            // テーブル要素が続く場合、直前の要素にappend
            LrdTableElement tableEl;
            
            if (core.getCurrElement() instanceof LrdTableElement) {
                tableEl = (LrdTableElement)core.getCurrElement();
            } else {
                tableEl = core.getFactory().createTableElement(core.getCurrParent());
                core.getCurrParent().appendElement(tableEl);
            }
            
            LrdRowElement rowEl = new LrdRowElement();
            for (String cell : cells) {
                rowEl.addText(new LrdTextElement(tableEl, Type.DEFAULT, cell), header);
            }
            tableEl.addRowElement(rowEl);
            
            core.setCurrElement(tableEl);
            return Parsed.YES;
        }
        return Parsed.NO;
    }
    

}
