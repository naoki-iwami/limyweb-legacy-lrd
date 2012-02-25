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

import org.limy.lrd.parser.bean.LrdTitleElement;

/**
 * @author Naoki Iwami
 */
/* package */ class LrdListParseSupport implements LrdParseSupport {

    public Parsed parse(LrdParserCore core, String line) throws IOException {

        if (!(isStartChar(line, '*') || isStartChar(line, ':') || isStartTag(line))) {
            return Parsed.NO;
        }

        int nowIndent = core.getIndent(line);
        core.adjustBlock(line);

        LrdTitleElement titleElement;
        String text;

        if (isStartChar(line, '*') || isStartChar(line, ':')) {
            LrdTitleElement.Type type = getType(line);
            titleElement = core.getFactory().createTitleElement(
                    core.getCurrParent(),
                    type, nowIndent + 2);
            text = line.substring(nowIndent + 1).trim();
        } else /* if (isStartTag(line)) */ {
            titleElement = core.getFactory().createTitleElement(
                    core.getCurrParent(),
                    LrdTitleElement.Type.TYPE_NUM, nowIndent + 4);
            text = line.substring(nowIndent + 4);
        }
        
        core.appendText(titleElement, text);
        core.getCurrParent().appendElement(titleElement);
        
        core.setCurrParent(titleElement);
        core.clearCurrElement();
        return Parsed.YES;
    }

    // ------------------------ Private Methods

    /**
     * 指定した文字で始まっているかを判定します。
     * @param line 
     * @param c 
     * @return
     */
    private boolean isStartChar(String line, char c) {
        
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == c) {
                return true;
            }
            if (line.charAt(i) != ' ') {
                return false;
            }
        }
        return false;
    }

    /**
     * (1) 形式で始まっているかを判定します。
     * @param line 
     * @return
     */
    private boolean isStartTag(String line) {
        String str = line.trim();
        if (str.length() >= 3 && str.charAt(0) == '(' && str.charAt(2) == ')') {
            return true;
        }
        return false;
    }

    private LrdTitleElement.Type getType(String line) {
        LrdTitleElement.Type type;
        if (isStartChar(line, ':')) {
            type = LrdTitleElement.Type.TYPE_1;
        } else {
            type = LrdTitleElement.Type.TYPE_2;
        }
        return type;
    }

}
