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
package org.limy.lrd.admin;

import org.limy.lrd.BaseLrdAction;
import org.limy.lrd.LrdConfig;
import org.limy.lrd.LrdRepositoryCreator;
import org.limy.lrd.common.ConfigRootBean;

import com.opensymphony.webwork.ServletActionContext;

/**
 * @author Naoki Iwami
 */
public abstract class AbstractAdminAction extends BaseLrdAction {

    // ------------------------ Fields (model)

    /** 設定ファイルインターフェイス */
    private LrdConfig config;

    /** リポジトリ作成担当 */
    private LrdRepositoryCreator repositoryCreator;

    // ------------------------ Fields

    /** 設定ファイルパス */
    private String configFilePath;

    protected abstract String doExecuteAdmin();

    // ------------------------ Bind Setter Methods (framework)

    /**
     * リポジトリ作成担当を設定します。
     * @param repositoryCreator リポジトリ作成担当
     */
    public void setRepositoryCreator(LrdRepositoryCreator repositoryCreator) {
        this.repositoryCreator = repositoryCreator;
    }

    // ------------------------ Override Methods

    @Override
    protected final String doExecute() {
        
        configFilePath = ServletActionContext.getServletContext()
                .getInitParameter("configFilePath").replaceAll("\\\\", "/");
        
        String result = ERROR;
        result = doExecuteAdmin();
        
        return result;
    }

    // ------------------------ Protected Methods
    
    protected void updateConfigRootBean() {
        config.updateConfigRootBean();
    }

    /**
     * リポジトリ作成担当を取得します。
     * @return リポジトリ作成担当
     */
    protected LrdRepositoryCreator getRepositoryCreator() {
        return repositoryCreator;
    }

    // ------------------------ Public Methods

    /**
     * 設定ファイルパスを取得します。
     * @return 設定ファイルパス
     */
    public String getConfigFilePath() {
        return configFilePath;
    }

    public ConfigRootBean getconfigRootBean() {
        return config.getConfigRootBean();
    }

    /**
     * 設定ファイルインターフェイスを設定します。
     * @param config 設定ファイルインターフェイス
     */
    public void setConfig(LrdConfig config) {
        this.config = config;
    }
    
}
