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

import org.limy.lrd.parser.bean.LrdNonElement;
import org.limy.lrd.parser.bean.LrdPreStrElement;

/**
 * @author Naoki Iwami
 */
public class LrdPreParseSupport implements LrdParseSupport {
    
    public Parsed parse(LrdParserCore core, String line) throws IOException {
        int indent = core.getCurrParent().getIndent();
        int nowIndent = core.getIndent(line);

        if (line.length() == 0
                && core.getCurrElement() instanceof LrdPreStrElement) {
            // pre中に空行があったら無視する
            return Parsed.YES;
        }
        
        if (nowIndent > indent) {
            LrdPreStrElement preStrElement;
            if (core.getCurrElement() instanceof LrdPreStrElement) {
                preStrElement = (LrdPreStrElement)core.getCurrElement();
            } else {
                preStrElement = core.getFactory().createPreStrElement(
                        core.getCurrParent(), nowIndent);
                core.getCurrParent().appendElement(preStrElement);
                core.setCurrElement(preStrElement);
            }
            preStrElement.appendWithBr(line);
            return Parsed.YES;
        }
        return Parsed.NO;
    }

}
