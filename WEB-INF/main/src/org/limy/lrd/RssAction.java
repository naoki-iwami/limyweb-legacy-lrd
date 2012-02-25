/*
 * Created 2007/07/08
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

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.rss.RssBean;
import org.limy.common.rss.RssChannel;
import org.limy.common.rss.RssItem;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdElement;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdParentElement;
import org.limy.lrd.common.LrdTextElement;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdHistory;

/**
 * RSS表示用のアクションクラスです。
 * @author Naoki Iwami
 */
public class RssAction extends BaseLrdAction {

    /** logger */
    private static final Log LOG = LogFactory.getLog(RssAction.class);
    
    // ------------------------ Fields (model)

    /** RSS表示件数 */
    private int showCount;
    
    /** DAO */
    private LrdDao dao;
    
    /** キャッシュマネージャ */
    private LrdCacheManager manager;

    // ------------------------ Fields
    
    /** リポジトリ名 */
    private String repositoryId;

    /** RSS Bean */
    private RssBean bean;

    // ------------------------ Public Methods
    
    @Override
    protected String doExecute() {
        
        bean = new RssBean();
        RssChannel channel = bean.getChannel();

        RepositoryBean repositoryBean = getModel().getRepositoryBean(repositoryId);
        if (repositoryBean == null) {
            return ERROR;
        }

        String rootUrl = repositoryBean.getMainDeployInfo().getDeployUrl() + "/";

        channel.setRdfAbout(rootUrl);
        channel.setTitle(repositoryBean.getName());
        channel.setLink(rootUrl);
        channel.setLanguage("ja");
        
        try {
            List<LrdHistory> records = dao.getLrdHistories(repositoryId, showCount);
            for (LrdHistory data : records) {
                
                CommitFileInfo lrdInfo = getModel().getRepository(repositoryId)
                        .getRepositoryFile(data.getLrdPath());
                
                LrdBean lrdBean = manager.getBean(repositoryId, lrdInfo);
                
                String title = lrdBean.getTitle();
                String desc = createDesc(lrdBean);
                
                RssItem item = new RssItem(rootUrl + data.getHtmlUrl(),
                        title, desc,
                        data.getCommitTime());
                bean.addItem(item);
            }
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
            return ERROR;
        } catch (LrdException e) {
            LOG.warn(e.getMessage(), e);
            return ERROR;
        }

        return SUCCESS;
    }
    
    // ------------------------ Getter/Setter Methods

    public RssBean getBean() {
        return bean;
    }

    /**
     * リポジトリ名を設定します。
     * @param repositoryName リポジトリ名
     */
    public void setRepositoryId(String repositoryName) {
        this.repositoryId = repositoryName;
    }

    /**
     * リポジトリ名を設定します。
     * @param repositoryName リポジトリ名
     */
    public void setId(String repositoryName) {
        this.repositoryId = repositoryName;
    }

    // ------------------------ Bind Setter Methods (framework)

    /**
     * RSS表示件数を設定します。
     * @param showCount RSS表示件数
     */
    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    /**
     * DAOを設定します。
     * @param dao DAO
     */
    public void setDao(LrdDao dao) {
        this.dao = dao;
    }

    /**
     * キャッシュマネージャを設定します。
     * @param manager キャッシュマネージャ
     */
    public void setManager(LrdCacheManager manager) {
        this.manager = manager;
    }

    // ------------------------ Private Methods

    private String createDesc(LrdBean lrdBean) {
        StringBuilder buff = new StringBuilder();
        appendDesc(buff, lrdBean.getRoot());
        return buff.toString();
    }

    private void appendDesc(StringBuilder buff, LrdElement root) {
        if (root instanceof LrdParentElement) {
            LrdParentElement el = (LrdParentElement)root;
            for (LrdElement child : el.getElements()) {
                appendDesc(buff, child);
            }
        }
        if (root instanceof LrdTextElement) {
            LrdTextElement textEl = (LrdTextElement)root;
            buff.append(textEl.getRawText()).append(' ');
        }
        
    }

}
