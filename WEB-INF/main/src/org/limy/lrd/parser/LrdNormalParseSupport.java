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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.lrd.parser.bean.LrdMultiTextElement;

/**
 * @author Naoki Iwami
 */
public class LrdNormalParseSupport implements LrdParseSupport {

    /** log */
    private static final Log LOG = LogFactory.getLog(LrdNormalParseSupport.class);
    
    public Parsed parse(LrdParserCore core, String line) throws IOException {
        if (line.length() == 0) {
            // 空行があったら、それまでのブロックを終了させる
            core.clearCurrElement();
        } else {
            LrdMultiTextElement textElement;
            if (core.getCurrElement() instanceof LrdMultiTextElement) {
                textElement = (LrdMultiTextElement)core.getCurrElement();
            } else {
                core.adjustBlock(line);
                
                textElement = core.getFactory().createMultiTextElement(core.getCurrParent());
                core.getCurrParent().appendElement(textElement);
                core.setCurrElement(textElement);
            }
            int indent = core.getCurrParent().getIndent();
            if (line.length() >= indent) {
                core.appendText(textElement, line.substring(indent));
            } else {
                LOG.warn(core.getBean().getPath().getRelativePath()
                        + " : !! '" + line + "' " + indent);
            }
        }
        return Parsed.YES;
    }

}
