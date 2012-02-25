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

import java.util.Collection;

import org.limy.lrd.common.LinkableTarget;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.LrdTargetable;
import org.limy.lrd.common.LrdTextElement;



/**
 * 見出し要素（1～4）を表します。
 * @author Naoki Iwami
 */
public class LrdChapterElement extends LrdMultiTextElement implements LrdTargetable {
    
    // ------------------------ Fields
    
    /**
     * 見出しレベル
     */
    private int level;

    // ------------------------ Constructors

    /**
     * LrdChapterElementインスタンスを構築します。
     * @param bean Lrd Bean
     * @param parent 親要素
     * @param level 見出しレベル
     */
    public LrdChapterElement(LrdBean bean, LrdElement parent, int level) {
        super(bean, parent);
        this.level = level;
    }

    // ------------------------ Implement Methods

    public void appendThis(LinkableTarget target, Collection<LrdTargetInfo> infos) {
        infos.add(new LrdTargetInfo(
                getTargetText(), target, getTargetId()));
    }

    public String getTargetText() {
        Collection<LrdTextElement> texts = getTexts();
        for (LrdTextElement text : texts) {
            return text.getRawText();
        }
        return null;
    }

    public String getTargetId() {
        return getId();
    }

    // ------------------------ Getter/Setter Methods

    /**
     * 見出しレベルを取得します。
     * @return 見出しレベル
     */
    public int getLevel() {
        return level;
    }

}
