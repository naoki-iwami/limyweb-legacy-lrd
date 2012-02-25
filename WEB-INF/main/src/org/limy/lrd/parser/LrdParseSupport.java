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

import java.io.IOException;

/**
 * パーツ単位のパースをサポートする内部インターフェイスです。
 * @author Naoki Iwami
 */
/* private */ interface LrdParseSupport {
    
    /**
     * パースしたかどうかの列挙型です。
     * @author Naoki Iwami
     */
    /* private */ enum Parsed {
        /** 処理した */
        YES,
        /** 処理しなかった */
        NO;
    };
    
    /**
     * 1行をパースします。
     * @param core パースコアインスタンス
     * @param line 1行
     * @return 処理したかどうか
     * @throws IOException I/O例外
     */
    Parsed parse(LrdParserCore core, String line) throws IOException;

}
