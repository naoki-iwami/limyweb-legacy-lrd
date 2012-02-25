/*
 * Created 2008/03/09
 * Copyright (C) 2003-2008  Naoki Iwami (naoki@limy.org)
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
package org.limy.lrd.deploy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

public class DynamicResourceLoader extends ResourceLoader {
    
    /** テンプレート内容 */
    private String contents;

    public DynamicResourceLoader(String contents) {
        this.contents = contents;
    }
    
    @Override
    public long getLastModified(Resource resource) {
        return System.currentTimeMillis();
    }

    @Override
    public InputStream getResourceStream(String source) throws ResourceNotFoundException {
        return new ByteArrayInputStream(contents.getBytes());
    }

    @Override
    public void init(ExtendedProperties configuration) {
        // do nothing
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return true;
    }
}