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

import org.limy.lrd.common.LrdBean;

/**
 * @author Naoki Iwami
 */
public class LrdOptionParseSupport implements LrdParseSupport {

    public Parsed parse(LrdParserCore core, String line) throws IOException {
        if (line.length() > 0 && line.charAt(0) == '#') {
            return Parsed.NO;
        }
        
        String[] lines = line.split("=");
        LrdBean bean = core.getBean();
        if (lines.length == 2) {
            String name = lines[0];
            String value = lines[1];
            
            setOptionValue(bean, name, value);
        }
        return Parsed.YES;
    }

    // ------------------------ Private Methods

    private void setOptionValue(LrdBean bean, String name, String value) {
        if ("title".equals(name)) {
            bean.setTitle(value);
        }
        if ("style".equals(name)) {
            bean.addStylesheet(value);
        }
        if ("print-time".equals(name)) {
            bean.setShowUpdateTime(Boolean.getBoolean(value));
        }
        if ("extcode".equals(name)) {
            bean.setExtCode(true);
        }
        if ("depend".equals(name)) {
            bean.addDependPath(value);
        }
        if ("ref".equals(name)) {
            bean.setRef(value);
        }
    }

}
