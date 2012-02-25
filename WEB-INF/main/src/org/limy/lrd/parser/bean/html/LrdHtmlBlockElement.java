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

import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.common.LrdElement;

/**
 * HTML出力可能なブロック要素を表します。
 * @author Naoki Iwami
 */
public class LrdHtmlBlockElement extends LrdBlockElement implements LrdHtmlWritable {

    /**
     * LrdHtmlBlockElementインスタンスを構築します。
     * @param bean Lrd Bean
     * @param parent 親要素
     * @param indent 基準インデント
     */
    public LrdHtmlBlockElement(LrdBean bean, LrdElement parent, int indent) {
        super(bean, parent, indent);
    }

    public void write(Appendable out) throws IOException {

        StringBuilder buff = new StringBuilder(); // <ol></ol>対策
        for (LrdElement element : getElements()) {
            if (element instanceof LrdHtmlWritable) {
                LrdHtmlWritable writableElement = (LrdHtmlWritable)element;
                writableElement.write(buff);
            }
        }
        out.append(buff);
    }

}
