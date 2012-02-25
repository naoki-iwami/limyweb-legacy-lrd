/*
 * Created 2007/07/29
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
package org.limy.lrd;

import org.limy.lrd.common.ConfigRootBean;
import org.limy.lrd.common.RepositoryBean;

/**
 * @author Naoki Iwami
 */
public interface LrdConfig {
    
    /**
     * 設定Beanを取得します。
     * @return 設定Bean
     */
    ConfigRootBean getConfigRootBean();
    
    void updateConfigRootBean();
    
    /**
     * リポジトリBeanを取得します。
     * @param repositoryId リポジトリID
     * @return リポジトリBean
     */
    RepositoryBean getRepositoryBean(String repositoryId);
    
}
