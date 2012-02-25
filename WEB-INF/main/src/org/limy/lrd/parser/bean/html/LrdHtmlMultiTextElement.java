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
import org.limy.lrd.common.LrdTextElement;
import org.limy.lrd.common.LrdTextElement.Type;
import org.limy.lrd.parser.bean.LrdMultiTextElement;

/**
 * HTML出力可能なテキスト要素を表します。
 * @author Naoki Iwami
 */
public class LrdHtmlMultiTextElement extends LrdMultiTextElement implements LrdHtmlWritable {

    /**
     * LrdHtmlMultiTextElementインスタンスを構築します。
     * @param parent 親要素
     */
    public LrdHtmlMultiTextElement(LrdElement parent) {
        super(parent.getBean(), parent);
    }

    // ------------------------ Implement Methods

    public void write(Appendable out) throws IOException {
        
        out.append("<p>");

        if (getBean().isExtCode() && getTexts().size() >= 2) {
            LrdTextElement el1 = getTexts().get(0);
            LrdTextElement el2 = getTexts().get(1);
            if (el1.getType() == Type.VARIABLE && el2.getText().length() == 1) {
//              // 先頭要素がVARIABLEで、次の要素が改行のみだった場合、extcodeを有効にする
                appendExtCodeHtml(out, el1);
            }
        }
        
        out.append(getText().replaceAll(BR, "<br />"));
        if (out instanceof StringBuilder) {
            StringBuilder buff = (StringBuilder)out;
            if (buff.length() >= 12
                    && "</div><br />".equals(buff.substring(buff.length() - 12))) {
                buff.setLength(buff.length() - 6);
            }
        }
        out.append("</p>").append(BR);
    }

    // ------------------------ Private Methods

    /**
     * extcodeブロックをHTML形式で出力します。
     * @param out 出力先
     * @param textEl テキスト要素
     */
    private void appendExtCodeHtml(Appendable out, LrdTextElement textEl) {
        if (out instanceof StringBuilder) {
            StringBuilder buff = (StringBuilder)out;
            buff.setLength(buff.length() - 3);
            buff.append("<code class=\"extcode\">");
            buff.append(textEl.getRawText());
            buff.append("</code><p>");
            // 先頭の2要素を削除
            getElements().remove(0);
            getElements().remove(0);
        }
    }

}
