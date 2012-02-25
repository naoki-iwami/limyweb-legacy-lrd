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

import java.util.Collection;



/**
 * 参照可能なターゲットを表します。
 * @author Naoki Iwami
 */
public interface LrdTargetable {
    
    /**
     * 参照文字列を返します。
     * @return 参照文字列
     */
    String getTargetText();
    
    /**
     * 参照先IDを返します。
     * @return 参照先ID
     */
    String getTargetId();
    
    /**
     * 参照情報リストに自身の情報を追加します。
     * @param target リンク可能なターゲット
     * @param infos 参照情報追加先
     */
    void appendThis(LinkableTarget target, Collection<LrdTargetInfo> infos);

}
