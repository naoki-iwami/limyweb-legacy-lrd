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

import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.parser.bean.LrdChapterElement;
import org.limy.lrd.parser.bean.LrdMultiTextElement;
import org.limy.lrd.parser.bean.LrdPreStrElement;
import org.limy.lrd.parser.bean.LrdTableElement;
import org.limy.lrd.parser.bean.LrdTitleElement;

/**
 * HTML用Lrd要素を生成するファクトリクラスです。
 * @author Naoki Iwami
 */
public class LrdHtmlElementFactory {
    
    /**
     * テキスト要素を生成します。
     * @param parent 親要素
     * @return テキスト要素
     */
    public LrdMultiTextElement createMultiTextElement(LrdElement parent) {
        return new LrdHtmlMultiTextElement(parent);
    }

//    /**
//     * テキスト要素を生成します。
//     * @param parent 親要素
//     * @return テキスト要素
//     * @param type 表示種別
//     * @param text 文字列
//     */
//    public LrdTextElement createTextElement(LrdElement parent, Type type, String text) {
//        return new LrdHtmlTextElement(parent, type, text);
//    }
    
    /**
     * ブロック要素を生成します。
     * @param parent 親要素
     * @param indent 基準インデント
     * @return ブロック要素
     */
    public LrdBlockElement createBlockElement(LrdElement parent, int indent) {
        LrdBean bean = null;
        if (parent != null) {
            bean = parent.getBean();
        }
        return new LrdHtmlBlockElement(bean, parent, indent);
    }


    /**
     * タイトル要素を生成します。
     * @param parent 親要素
     * @param type 種別
     * @param indent 基準インデント
//     * @param text 文字列
     * @return タイトル要素
     */
    public LrdTitleElement createTitleElement(
            LrdElement parent, LrdTitleElement.Type type, int indent/*, String text*/) {
        return new LrdHtmlTitleElement(parent, type, indent/*, text*/);
    }

    /**
     * 見出し要素を生成します。
     * @param bean Lrd Bean
     * @param parent 親要素
     * @param level 見出しレベル
     * @return 見出し要素
     */
    public LrdChapterElement createChapterElement(LrdElement parent, int level) {
        return new LrdHtmlChapterElement(parent, level);
    }

    /**
     * PREテキスト要素を生成します。
     * @param parent 親要素
     * @param indent 基準インデント
     * @return PREテキスト要素
     */
    public LrdPreStrElement createPreStrElement(LrdElement parent, int indent) {
        return new LrdHtmlPreStrElement(parent, indent);
    }

    /**
     * テーブル要素を生成します。
     * @param parent 親要素
     * @return テーブル要素
     */
    public LrdTableElement createTableElement(LrdElement parent) {
        return new LrdHtmlTableElement(parent);
    }

}
