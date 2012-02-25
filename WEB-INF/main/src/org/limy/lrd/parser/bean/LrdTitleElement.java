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

import java.util.ArrayList;
import java.util.Collection;

import org.limy.lrd.common.DynamicTargetResolver;
import org.limy.lrd.common.LinkableTarget;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.common.LrdParentElement;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.LrdTargetable;
import org.limy.lrd.common.LrdTextElement;



/**
 * タイトル要素を表します。
 * @author Naoki Iwami
 */
public class LrdTitleElement extends LrdBlockElement
        implements LrdTargetable, DynamicTargetResolver, LrdParentElement {
    
    /**
     * タイトル種別を表します。
     */
    public enum Type {
        
        /** type1 */
        TYPE_1,
        // 「: label」タイプ。<dl> 〜 </dl> を表す
        
        /** type2 */
        TYPE_2,
        // 「* label」タイプ。<ul> 〜 </ul> を表す
        
        /** type-num */
        TYPE_NUM,
        // 「(1) label」タイプ。<ol> 〜 </ol> を表す
        
    }
    
    // ------------------------ Fields
    
    /**
     * タイトル種別
     */
    private Type type;
    
    /**
     * テキスト要素リスト
     */
    private Collection<LrdTextElement> texts = new ArrayList<LrdTextElement>();
//    /**
//     * 文字列
//     */
//    private String text;
    
    // ------------------------ Constructors

    /**
     * LrdTitleElementインスタンスを構築します。
     * @param bean Lrd Bean
     * @param parent 親要素
     * @param type タイトル種別
     * @param indent 基準インデント
//     * @param text 文字列
     */
    public LrdTitleElement(LrdBean bean,
            LrdElement parent, Type type, int indent/*, String text*/) {
        super(bean, parent, indent);
        this.type = type;
//        this.text = text;
    }
    
    // ------------------------ Implement Methods
    
    public void appendThis(LinkableTarget target, Collection<LrdTargetInfo> infos) {
        infos.add(new LrdTargetInfo(
                getTargetText(), target, getTargetId()));
        for (LrdElement element : getElements()) {
            if (element instanceof LrdTargetable) {
                ((LrdTargetable)element).appendThis(target, infos);
            }
        }
    }

    public String getTargetText() {
        for (LrdTextElement text : texts) {
            if (text.getRawText() != null) {
                return text.getRawText();
            }
        }
        return "";
    }
    
    public String getTargetId() {
        return getId();
    }
    
    public void decideDynamicTarget(LrdBean bean, Collection<LrdTargetInfo> infos) {
        for (LrdTextElement element : texts) {
            if (element instanceof DynamicTargetResolver) {
                ((DynamicTargetResolver)element).decideDynamicTarget(bean, infos);
            }
        }
        for (LrdElement element : getElements()) {
            if (element instanceof DynamicTargetResolver) {
                ((DynamicTargetResolver)element).decideDynamicTarget(bean, infos);
            }
        }
    }

    // ------------------------ Override Methods

    @Override
    public void appendElement(LrdElement element) {
        if (element instanceof LrdTextElement) {
            texts.add((LrdTextElement)element);
        } else {
            super.appendElement(element); // LrdBlockElement.appendElement
        }
    }

    // ------------------------ Getter/Setter Methods

    /**
     * タイトル種別を取得します。
     * @return タイトル種別
     */
    public Type getType() {
        return type;
    }

    /**
     * 文字列を取得します。
     * @return 文字列
     */
    public String getText() {
        StringBuilder buff = new StringBuilder();
        for (LrdTextElement element : texts) {
            buff.append(element.getText());
        }
        return buff.toString();
    }

}
