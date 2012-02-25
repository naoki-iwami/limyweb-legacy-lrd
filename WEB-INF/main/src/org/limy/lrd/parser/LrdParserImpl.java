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
package org.limy.lrd.parser;

import java.io.BufferedReader;
import java.io.IOException;

import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdParser;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.parser.bean.html.LrdHtmlElementFactory;

/**
 * Lrdファイルのパーサクラスです。
 * @author Naoki Iwami
 */
public class LrdParserImpl implements LrdParser {
    
    // ------------------------ Fields

    /** Lrdファクトリ */
    private LrdHtmlElementFactory factory = new LrdHtmlElementFactory();

    // ------------------------ Public Methods

    /**
     * 入力を解析してLrdBeanインスタンスを生成します。
     * @param repository リポジトリ情報
     * @param path パス情報
     * @param reader リーダ
     * @return 生成されたLrdBean
     * @throws IOException I/O例外
     */
    public LrdBean parse(LrdRepository repository,
            LrdPath path, BufferedReader reader) throws IOException {
        
        LrdParserCore core = new LrdParserCore(repository, factory);
        return core.parse(path, reader);
    }

}
