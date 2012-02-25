/*
 * Created 2007/08/17
 * Copyright (C) 2003-2007  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limyweb-lrd.
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
package org.limy.lrd.parser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Naoki Iwami
 */
public final class LrdParseSupportFactory {

    /** 唯一のインスタンス */
    private static final LrdParseSupportFactory instance = new LrdParseSupportFactory();
    
    /** サポート一覧 */
    private final Collection<LrdParseSupport> supports;

    /** ヘッダサポート一覧 */
    private final Collection<LrdParseSupport> headerSupports;

    /**
     * private constructor
     */
    private LrdParseSupportFactory() {
        supports = new ArrayList<LrdParseSupport>();
        supports.add(new LrdChapterParseSupport());
        supports.add(new LrdPreParseSupport());
        supports.add(new LrdListParseSupport());
        supports.add(new LrdTableParseSupport());
        supports.add(new LrdNormalParseSupport());
        
        headerSupports = new ArrayList<LrdParseSupport>();
        headerSupports.add(new LrdOptionParseSupport());

    }
    
    public static LrdParseSupportFactory getInstance() {
        return instance;
    }

    public Collection<LrdParseSupport> getSupports() {
        return supports;
    }

    public Collection<LrdParseSupport> getHeaderSupports() {
        return headerSupports;
    }

}
