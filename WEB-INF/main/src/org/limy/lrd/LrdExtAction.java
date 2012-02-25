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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.limy.common.util.UrlUtils;
import org.limy.common.web.WebworkUtils;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;

import com.opensymphony.webwork.ServletActionContext;

/**
 * @author Naoki Iwami
 */
public class LrdExtAction extends BaseLrdAction {

    // ------------------------ Constants

    /** serialVersionUID */
    private static final long serialVersionUID = 2934148168380767818L;


    // ------------------------ Fields

    /** リポジトリID */
    private String id;
    
    /** Lrdパス */
    private String lrdPath;

    // ------------------------ Implement Methods

    @Override
    protected String doExecute() {
        return SUCCESS;
    }
    
    // ------------------------ Public Methods

    public Collection<LrdDeployInfo> getDeployInfos() {
        return getModel().getRepositoryBean(id).getDeployInfos();
    }

    // ------------------------ Web Getter Methods

    public String getLogoutUrl() {
        String referer = ServletActionContext.getRequest().getHeader("referer");
        if (referer == null || referer.startsWith(WebworkUtils.getContextRootUrl())) {
            // 管理画面からの場合
            return "./";
        }
        // それ以外からの遷移は、元のページにLogoutさせる
        return referer;
    }
    
    public Collection<String> getAttachFiles() throws LrdException {
        Collection<String> results = new ArrayList<String>();
        LrdRepository repository = getModel().getRepository(id);
        LrdNode root = repository.getDirectoryRoot();
        LrdNode targetNode = root.searchSubNodeDepth(UrlUtils.getParent(lrdPath));
        for (LrdNode node : targetNode.getSubNodes()) {
            String path = node.getPath().getName();
            if (!node.isFolder() && !path.endsWith(".lrd")) {
                results.add(path);
            }
        }
        
        return results;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * リポジトリIDを取得します。
     * @return リポジトリID
     */
    public String getId() {
        return id;
    }

    /**
     * リポジトリIDを設定します。
     * @param id リポジトリID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Lrdパスを取得します。
     * @return Lrdパス
     */
    public String getLrdPath() {
        return lrdPath;
    }

    /**
     * Lrdパスを設定します。
     * @param lrdPath Lrdパス
     */
    public void setLrdPath(String lrdPath) {
        this.lrdPath = lrdPath;
    }

}
