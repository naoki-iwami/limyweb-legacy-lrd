/*
 * Created 2006/04/30
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
import org.limy.lrd.parser.bean.LrdTitleElement;

/**
 *
 * @author Naoki Iwami
 */
public class LrdHtmlTitleElement extends LrdTitleElement implements LrdHtmlWritable  {

    /**
     * LrdHtmlTitleElementインスタンスを構築します。
     * @param parent 親要素
     * @param type 種別
     * @param indent 基準インデント
//     * @param text 文字列
     */
    public LrdHtmlTitleElement(LrdElement parent, Type type, int indent/*, String text*/) {
        super(parent.getBean(), parent, type, indent/*, text*/);
    }

    public void write(Appendable out) throws IOException {
        
        switch (getType()) {
        case TYPE_1:
            writeType1(out);
            break;
        case TYPE_2:
            writeType2(out);
            break;
        case TYPE_NUM:
            writeTypeNum(out);
            break;
        default:
            throw new IllegalStateException("Not Support type : " + getType());
        }
    }

    // ------------------------ Private Methods

    private void writeTypeNum(Appendable out) throws IOException {
        if (out instanceof StringBuilder) {
            StringBuilder buff = (StringBuilder) out;
            if (buff.length() >= 5 && "</ol>".equals(buff.substring(buff.length() - 5))) {
                buff.setLength(buff.length() - 5);
            } else if (buff.length() >= 14
                    && "</ol></dd><dd>".equals(buff.substring(buff.length() - 14))) {
                buff.setLength(buff.length() - 14);
            } else {
                out.append("<ol>");
            }
        } else {
            out.append("<ol>");
        }
        writeLiElement(out);
        out.append("</ol>");
    }

    private void writeType2(Appendable out) throws IOException {
        out.append("<ul>");
        writeLiElement(out);
        out.append("</ul>");
    }

    private void writeType1(Appendable out) throws IOException {
        out.append("<dl>");
        out.append("<dt>");
        
        out.append("<a name=\"").append(getTargetId()).append("\">");
        out.append(getText());
        out.append("</a>");
        
        out.append("</dt>");
        for (LrdElement element : super.getElements()) {
            if (element instanceof LrdHtmlWritable) {
                LrdHtmlWritable writableElement = (LrdHtmlWritable)element;
                out.append("<dd><p>");
                writableElement.write(out);
                out.append("</p></dd>");
            }
        }
        out.append("</dl>");
    }

    private void writeLiElement(Appendable out) throws IOException {
        out.append("<li>");
        out.append("<p><a name=\"").append(getTargetId()).append("\">");
        out.append(getText());
        out.append("</a></p>");
        for (LrdElement element : getElements()) {
            if (element instanceof LrdHtmlWritable) {
                LrdHtmlWritable writableElement = (LrdHtmlWritable)element;
                writableElement.write(out);
            }
        }
        out.append("</li>");
    }

}
