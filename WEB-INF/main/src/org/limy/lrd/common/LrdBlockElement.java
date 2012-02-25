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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;




/**
 * Lrdのブロック要素を表します。
 * @author Naoki Iwami
 */
public class LrdBlockElement extends LrdBaseElement
        implements LrdParentElement {
    
    // ------------------------ Fields
    
    /**
     * 子要素
     */
    private List<LrdElement> elements = new ArrayList<LrdElement>();

    /**
     * 基準インデント
     */
    private int indent;
    
    // ------------------------ Constructors

    /**
     * LrdBlockElementインスタンスを構築します。
     * @param bean Lrd Bean
     * @param parent 親要素
     * @param indent 基準インデント
     */
    public LrdBlockElement(LrdBean bean, LrdElement parent, int indent) {
        super(bean, parent);
        this.indent = indent;
    }
    
    // ------------------------ Override Methods

    @Override
    public int setNumberingId(String prefix, int number) {
        int tmpNumber = number;
        setId(prefix + tmpNumber);
        ++tmpNumber;
        for (LrdElement element : elements) {
            if (element instanceof IdentifyElement) {
                tmpNumber = ((IdentifyElement)element).setNumberingId(prefix, tmpNumber);
            }
        }
        return tmpNumber;
    }

    // ------------------------ Implement Methods

    public void appendElement(LrdElement element) {
        if (element != null) {
            elements.add(element);
        }
    }
    
    public Collection<? extends LrdElement> getElements() {
        return elements;
    }

    public ListIterator<? extends LrdElement> listIterator() {
        return elements.listIterator();
    }
    
    // ------------------------ Public Methods

//    //@Override
//    public Collection<LrdElement> getSubElements() {
//        return elements;
//    }

//    //@Override
//    public void addElement(LrdElement element) {
//        if (element != null) {
//            elements.add(element);
//        }
//    }

    // ------------------------ Getter/Setter Methods

    /**
     * 基準インデントを取得します。
     * @return 基準インデント
     */
    public int getIndent() {
        return indent;
    }


}
