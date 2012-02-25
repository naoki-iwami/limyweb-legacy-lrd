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





/**
 * 全Lrd要素の基底クラスです。
 * @author Naoki Iwami
 */
public class LrdBaseElement implements LrdElement, IdentifyElement {
    
    // ------------------------ Constructors

    /**
     * 改行文字
     */
    protected static final String BR = "\n";

    // ------------------------ Fields
    
    /**
     * Lrd Bean
     */
    private LrdBean bean;
    
    /**
     * 親要素
     */
    private LrdElement parent;
    
    /**
     * ID
     */
    private String id;
    
    // ------------------------ Constructors
    
    /**
     * LrdBaseElementインスタンスを構築します。
     * @param bean Lrd Bean
     * @param parent 親要素
     */
    public LrdBaseElement(LrdBean bean, LrdElement parent) {
        super();
        this.bean = bean;
        this.parent = parent;
    }
    
    // ------------------------ Implement Methods

    public LrdBean getBean() {
        return bean;
    }

    public LrdElement getParent() {
        return parent;
    }

    public String getId() {
        return id;
    }

    public int setNumberingId(String prefix, int number) {
        this.id = prefix + number;
        return number + 1;
    }
    
    // ------------------------ Protected Methods
    
    protected void setId(String id) {
        this.id = id;
    }

    // ------------------------ Public Methods

    /**
     * Lrd Beanを設定します。
     * @param bean Lrd Bean
     */
    public void setBean(LrdBean bean) {
        this.bean = bean;
    }

}
