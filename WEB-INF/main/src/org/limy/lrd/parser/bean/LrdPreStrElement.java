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
package org.limy.lrd.parser.bean;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.limy.lrd.common.LrdBaseElement;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdElement;

/**
 * PREテキスト要素を表します。
 * @author Naoki Iwami
 */
public class LrdPreStrElement extends LrdBaseElement {
    
    // ------------------------ Constants

    /**
     * logger
     */
    private static final Logger logger = Logger.getLogger(LrdPreStrElement.class);
    
    // ------------------------ Fields

    /**
     * 基準インデント
     */
    private int indent;
    
    /**
     * 文字列バッファ
     */
    private Appendable buff;

    // ------------------------ Constructors

    /**
     * LrdPreStrElementインスタンスを構築します。
     * @param bean LrdBean
     * @param parent 親要素
     * @param indent 基準インデント
     */
    public LrdPreStrElement(LrdBean bean, LrdElement parent, int indent) {
        super(bean, parent);
        this.indent = indent;
        buff = new StringBuilder();
    }

    // ------------------------ Override Methods

    @Override
    public String toString() {
        return buff.toString();
    }

    // ------------------------ Public Methods

    /**
     * 文字列を改行付きで追加します。
     * @param line 文字列
     * @throws IOException I/O例外
     */
    public void appendWithBr(String line) throws IOException {
        if (line.length() < indent) {
            logger.error("line = '" + line + "', indent = " + indent);
            buff.append(BR);
            return;
        }
        buff.append(line.substring(indent)).append(BR);
    }

    /**
     * 文字列形式で取得します。
     * @return 文字列
     */
    public String getText() {
        return buff.toString();
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * 基準インデントを取得します。
     * @return 基準インデント
     */
    public int getIndent() {
        return indent;
    }

}
