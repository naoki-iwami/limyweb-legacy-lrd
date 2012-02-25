/*
 * Created 2007/08/05
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
package org.limy.lrd.repository;

import org.limy.lrd.LrdConfig;
import org.limy.lrd.LrdRepositoryCreator;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.RepositoryBean.RepositoryType;
import org.limy.lrd.repository.file.LrdFileRepository;
import org.limy.lrd.repository.svn.LrdSvnRepository;

/**
 * @author Naoki Iwami
 */
public class LrdRepositoryCreatorImpl implements LrdRepositoryCreator {

    // ------------------------ Fields

    /** LrdConfig */
    private LrdConfig config;

    // ------------------------ Bind Setter Methods (framework)

    /**
     * LrdConfigを設定します。
     * @param config LrdConfig
     */
    public void setConfig(LrdConfig config) {
        this.config = config;
    }
    
    // ------------------------ Initializer Methods
    
    public void init() {
        for (RepositoryBean bean : config.getConfigRootBean().getRepositoryBeans()) {
            if (bean.getType() == RepositoryType.FILE) {
                bean.setRepository(new LrdFileRepository(bean));
            } else {
                bean.setRepository(new LrdSvnRepository(bean));
            }
        }
    }

    // ------------------------ Implement Methods

    public void refreshRepositoryBeans() {
        init();
    }

}
