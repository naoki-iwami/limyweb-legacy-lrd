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

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.limy.lrd.common.ConfigRootBean;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdDeployInfo.DeployType;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.opensymphony.webwork.config.ServletContextSingleton;

/**
 * @author Naoki Iwami
 */
public class LrdConfigImpl implements LrdConfig {

    /** 空のリポジトリBean（ダミー用） */
    private static final RepositoryBean EMPTY_REPOSITORY_BEAN = new RepositoryBean();

    // ------------------------ Fields

    /** 設定Bean */
    private ConfigRootBean configRootBean = new ConfigRootBean();
    
    /** 設定ファイル */
    private File configFile;

    // ------------------------ Constructors
    
    public void init() {
    
        ServletContext context = ServletContextSingleton.getInstance().getServletContext();
        String configFilePath = context.getInitParameter("configFilePath");
        
        configFile = new File(configFilePath);
        if (!configFile.exists()) {
            ObjectContainer db = Db4o.openFile(configFilePath);
            db.set(configRootBean);
            db.close();
        }

        ObjectContainer db = Db4o.openFile(configFilePath);
        configRootBean = db.query(ConfigRootBean.class).iterator().next();
        Collection<RepositoryBean> beans = configRootBean.getRepositoryBeans();
        boolean tmpSave = false;
        
        for (RepositoryBean bean : beans) {
            Collection<LrdDeployInfo> infos = bean.getDeployInfos();
            LrdDeployInfo newInfo = new LrdDeployInfo(DeployType.LOCAL, "LOCAL", "http://localhost:8090/limyweb-lrd/context/PROGRAM");
            newInfo.setName("LOCAL");
            ((List<LrdDeployInfo>)infos).set(0, newInfo);
            tmpSave = true;
//            if (infos.size() == 3) {
//                ((List<LrdDeployInfo>)infos).remove(2);
//            }
//            if (infos.size() == 2) {
//                LrdDeployInfo info = new LrdDeployInfo(DeployType.LOCAL, "sakura", "http://www.limy.org/program");
//                info.setName("sakura");
//                infos.add(info);
//                tmpSave = true;
//            }
        }
        
        db.close();

        if (tmpSave) updateConfigRootBean();

    }
    
    // ------------------------ Implement Methods

    /**
     * 設定Beanを取得します。
     * @return 設定Bean
     */
    public ConfigRootBean getConfigRootBean() {
        return configRootBean;
    }
    
    public void updateConfigRootBean() {
        synchronized (configFile) {
            configFile.delete();
            ObjectContainer db = Db4o.openFile(configFile.getAbsolutePath());
            db.set(configRootBean);
            db.close();
        }
    }

    public RepositoryBean getRepositoryBean(String repositoryId) {
        for (RepositoryBean bean : configRootBean.getRepositoryBeans()) {
            if (bean.getId().equals(repositoryId)) {
                return bean;
            }
        }
        return EMPTY_REPOSITORY_BEAN;
    }

}
