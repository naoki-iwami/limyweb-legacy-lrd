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
package org.limy.lrd.parser.bean.html;

import java.io.IOException;

import org.limy.lrd.common.LrdElement;
import org.limy.lrd.parser.bean.LrdCellElement;
import org.limy.lrd.parser.bean.LrdRowElement;
import org.limy.lrd.parser.bean.LrdTableElement;

/**
 * HTML出力可能なテーブル要素を表します。
 * @author Naoki Iwami
 */
public class LrdHtmlTableElement extends LrdTableElement implements LrdHtmlWritable {

    public LrdHtmlTableElement(LrdElement parent) {
        super(parent.getBean(), parent);
    }

    public void write(Appendable out) throws IOException {
        out.append("<p>").append(BR);
        out.append("<table class=\"bodyTable limyTable\" border=\"0\">").append(BR);
        for (LrdRowElement rowEl : getElements()) {
            out.append("<tr>").append(BR);
            for (LrdCellElement cellEl : rowEl.getCells()) {
                if (cellEl.isHeader()) {
                    out.append("<th>");
                    out.append(cellEl.getTextEl().getText());
                    out.append("</th>");
                } else {
                    out.append("<td>");
                    out.append(cellEl.getTextEl().getText());
                    out.append("</td>");
                }
                out.append(BR);
            }
            out.append("</tr>").append(BR);
        }
        out.append("</table>").append(BR);
        out.append("</p>").append(BR);
    }

}
