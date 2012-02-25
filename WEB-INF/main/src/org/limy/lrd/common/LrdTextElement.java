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
package org.limy.lrd.common;

import java.io.IOException;

import org.limy.common.util.HtmlUtils;


/**
 * テキスト要素を表します。
 * @author Naoki Iwami
 */
public class LrdTextElement extends LrdBaseElement {
    
    /**
     * 表示種別
     */
    public enum Type {
        
        /** 通常 */
        DEFAULT,

        /** 強調 */
        STRONG,
        
        /** 変数 */
        VARIABLE,

        /** プレイン */
        PLAIN,

        /** リンク */
        LINK,
        
    }
    
    // ------------------------ Fields

    /**
     * 文字列バッファ
     */
    private Appendable buff;
    
    /**
     * 表示種別
     */
    private Type type = Type.DEFAULT;
    
    // ------------------------ Constructors

    /**
     * LrdTextElementインスタンスを構築します。
     * @param parent 親要素
     * @param type 表示種別
     * @param text 文字列
     */
    public LrdTextElement(LrdElement parent, Type type, String text) {
        super(parent.getBean(), parent);
        this.type = type;
        buff = new StringBuilder(text);
    }

    // ------------------------ Override Methods

    @Override
    public String toString() {
        return buff.toString();
    }
    
    // ------------------------ Public Methods
    
    public void setText(String text) {
        buff = new StringBuilder(text);
    }
    
    /**
     * 表示種別を取得します。
     * @return 表示種別
     */
    public Type getType() {
        return type;
    }
    
    /**
     * 文字列を改行付きで追加します。
     * @param line 文字列
     * @throws IOException I/O例外
     */
    public void appendWithBr(String line) throws IOException {
        buff.append(line).append(BR);
    }
    
    /**
     * 文字列形式で取得します。
     * @return 文字列
     */
    public String getText() {
        String result;
        switch (type) {
        case STRONG:
            result = "<strong>" + getHtmlText() + "</strong>";
            break;
        case VARIABLE:
            result = "<code>" + getHtmlText() + "</code>";
            break;
        case PLAIN:
            result = getRawText();
            break;
        case LINK:
            result = ((LrdLinkElement)this).getLinkText();
            break;
        case DEFAULT:
            result = getHtmlText();
            break;
        default:
            throw new IllegalStateException("Not support type : " + type);
        }
        return result;
    }

    /**
     * 生の文字列を返します。
     * @return 生の文字列
     */
    public String getRawText() {
        return buff.toString();
    }
    
    // ------------------------ Private Methods

    /**
     * HTML表示文字列を返します。
     * @return HTML表示文字列
     */
    private String getHtmlText() {
        return HtmlUtils.quoteHtml(getRawText());
    }
    

}
