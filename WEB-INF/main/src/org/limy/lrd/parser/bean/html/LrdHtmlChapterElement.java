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
package org.limy.lrd.parser.bean.html;

import java.io.IOException;

import org.limy.lrd.common.LrdElement;
import org.limy.lrd.parser.bean.LrdChapterElement;

/**
 * HTML出力可能な見出し要素を表します。
 * @author Naoki Iwami
 */
public class LrdHtmlChapterElement extends LrdChapterElement implements LrdHtmlWritable {

    /**
     * LrdHtmlChapterElementインスタンスを構築します。
     * @param parent 親要素
     * @param level 見出しレベル
     */
    public LrdHtmlChapterElement(LrdElement parent, int level) {
        super(parent.getBean(), parent, level);
    }

//    /**
//     * LrdHtmlChapterElementインスタンスを構築します。
//     * @param parent 親要素
//     * @param level 見出しレベル
//     * @param text 見出し文字
//     */
//    public LrdHtmlChapterElement(LrdElement parent, int level, String text) {
//        super(parent, level, text);
//    }

    // ------------------------ Implement Methods

    public void write(Appendable out) throws IOException {
        out.append("<a name=\"").append(getTargetId()).append("\"></a>");
        out.append("<h").append(Integer.toString(getLevel()));
        out.append(">");
//        out.append(getTargetText());
        out.append(getText());
        out.append("</h").append(Integer.toString(getLevel()));
        out.append(">").append(BR);
        
    }

}
