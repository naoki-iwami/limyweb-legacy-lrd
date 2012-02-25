/*
 * Created 2007/06/16
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
package org.limy.lrd.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.limy.common.web.WebworkUtils;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.VelocityEngineModel;

/**
 * Velocityエンジンを扱うモデル実装クラスです。
 * @author Naoki Iwami
 */
public class VelocityEngineModelImpl implements VelocityEngineModel {
    
    /** logger */
    private static final Log LOG = LogFactory.getLog(VelocityEngineModelImpl.class);
    
    // ------------------------ Fields

    /** リポジトリ毎のVelocityエンジン */
    private Map<String, VelocityEngine> engines = new HashMap<String, VelocityEngine>();

    /** Velocity設定ファイルパス */
    private String propFile;
    
    // ------------------------ Bind Setter Methods (framework)

    /**
     * Velocity設定ファイルパスを設定します。
     * @param propFile Velocity設定ファイルパス
     */
    public void setPropFile(String propFile) {
        this.propFile = propFile;
    }
    
    // ------------------------ Implement Methods

    /**
     * Velocityエンジンを取得します。
     * @return Velocityエンジン
     */
    public VelocityEngine getEngine(RepositoryBean repositoryBean) {
        VelocityEngine engine = engines.get(repositoryBean.getId());
        if (engine == null) {
            synchronized (VelocityEngineModelImpl.class) {
                InputStream in = null;
                try {
                    Properties prop = new Properties();
                    in = new FileInputStream(WebworkUtils.getFile(propFile));
                    prop.load(in);
                    prop.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH,
                            new File(repositoryBean.getLocalContentFile(),
                                    ".template/vm").getAbsolutePath()
                    );
                    engine = new VelocityEngine(prop);
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                }
            }
            engines.put(repositoryBean.getId(), engine);
        }
        return engine;
    }

    public void refreshEngine(RepositoryBean repositoryBean) {
        engines.remove(repositoryBean.getId());
    }

}
