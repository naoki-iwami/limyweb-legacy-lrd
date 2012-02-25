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
import java.util.List;
import java.util.ListIterator;

import org.limy.lrd.common.DynamicTargetResolver;
import org.limy.lrd.common.LrdBaseElement;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.common.LrdParentElement;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.LrdTextElement;



/**
 * テキスト要素を表します。
 * @author Naoki Iwami
 */
public class LrdMultiTextElement extends LrdBaseElement
        implements DynamicTargetResolver, LrdParentElement {

    // ------------------------ Fields

    /**
     * テキスト要素リスト
     */
    private List<LrdTextElement> texts = new ArrayList<LrdTextElement>();
    
    // ------------------------ Constructors

    /**
     * LrdMultiTextElementインスタンスを構築します。
     * @param bean Lrd Bean
     * @param parent 親要素
     */
    public LrdMultiTextElement(LrdBean bean, LrdElement parent) {
        super(bean, parent);
    }
    
    // ------------------------ Implement Methods

    public void decideDynamicTarget(LrdBean bean, Collection<LrdTargetInfo> infos) {
        for (LrdTextElement element : texts) {
            if (element instanceof DynamicTargetResolver) {
                DynamicTargetResolver linkElement = (DynamicTargetResolver)element;
                linkElement.decideDynamicTarget(bean, infos);
            }
        }
    }


    public void appendElement(LrdElement element) {
        texts.add((LrdTextElement)element);
    }
    
    public List<? extends LrdElement> getElements() {
        return texts;
    }

    public ListIterator<? extends LrdElement> listIterator() {
        return texts.listIterator();
    }

    // ------------------------ Override Methods
    
//    @Override
//    public String toString() {
//        return buff.toString();
//    }
    
//    @Override
//    public void addElement(LrdElement element) {
//        throw new IllegalArgumentException("Not support 'addElement' method."
//                + "Use 'appendElement' instead of.");
//    }

    // ------------------------ Public Methods
    
    /**
     * 文字列形式で取得します。
     * @return 文字列
     */
    public String getText() {
        StringBuilder buff = new StringBuilder();
        for (LrdTextElement element : texts) {
            buff.append(element.getText());
        }
        return buff.toString();
    }
    
    // ------------------------ Protected Methods

    /**
     * テキスト要素リストを取得します。
     * @return テキスト要素リスト
     */
    protected List<LrdTextElement> getTexts() {
        return texts;
    }
    
}
