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

import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.parser.bean.LrdChapterElement;

/**
 * Chapter のパースをサポートする内部クラスです。
 * @author Naoki Iwami
 */
/* package */ class LrdChapterParseSupport implements LrdParseSupport {

    public Parsed parse(LrdParserCore core, String line) throws IOException {
        if (line.length() > 0 && line.charAt(0) == '=') {
            
            int indent = core.getCurrParent().getIndent();
            int nowIndent = core.getIndent(line);
            while (nowIndent < indent) {
                // インデントが減っていたら、親要素から抜ける
                core.setCurrParent((LrdBlockElement)core.getCurrParent().getParent());
                indent = core.getCurrParent().getIndent();
            }

            int level = 0;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) != '=') {
                    level = i;
                    break;
                }
            }
            String text = line.substring(level).trim();
            LrdChapterElement chapterElement = core.getFactory().createChapterElement(
                    core.getCurrParent(), level);
            core.appendText(chapterElement, text);
            core.getCurrParent().appendElement(chapterElement);
            
            core.clearCurrElement();
            return Parsed.YES;
        }
        return Parsed.NO;
    }

}
