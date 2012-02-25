/*
 * Created 2007/08/15
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

import java.util.Date;

import org.limy.common.LimyException;
import org.limy.lrd.LrdConfig;
import org.limy.lrd.LrdDao;
import org.limy.lrd.LrdDbModel;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.AdminLog;
import org.limy.lrd.common.bean.LrdCommentInfo;
import org.limy.lrd.common.bean.LrdHistory;
import org.limy.lrd.common.util.LrdUtils;

/**
 * データベース関連のモデル実装クラスです。
 * @author Naoki Iwami
 */
public class LrdDbModelImpl implements LrdDbModel {
    
    // ------------------------ Fields (model)

    /** LrdConfig */
    private LrdConfig config;

    /** DAO */
    private LrdDao dao;

    // ------------------------ Implement Methods

    /**
     * LrdファイルをRSSのアイテムとして追加します。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @throws LrdException 共通例外
     */
    public void addRss(String repositoryId, String lrdPath) throws LrdException {
        LrdHistory history = new LrdHistory();
        history.setLrdPath(lrdPath);
        
        String uploadPath = LrdUtils.getHtmlPath(lrdPath);
//        String uploadPath = UrlUtils.getParent(lrdPath);
//        uploadPath = UrlUtils.concatUrl(uploadPath,
//                UrlUtils.getBaseName(lrdPath) + ".html");
        history.setHtmlUrl(uploadPath);

        RepositoryBean repositoryBean = config.getRepositoryBean(repositoryId);
        
        Date timestamp = repositoryBean.getRepository().getTimestamp(lrdPath,
                repositoryBean.getUserName(), repositoryBean.getPassword());
        history.setCommitTime(timestamp);
        
        dao.insertLrdHistory(repositoryId, history);
    }

    /**
     * コメントを投稿します。
     * @param repositoryId リポジトリID
     * @param info コメント情報
     * @throws LimyException 共通例外
     */
    public void submitLrdComment(String repositoryId, LrdCommentInfo info) throws LimyException {
        dao.insertLrdComment(repositoryId, info);
        AdminLog adminLog = new AdminLog();
        adminLog.setUserId(1); // TODO
        adminLog.setTargetTimestamp(new Date());
        adminLog.setAction("submitLrdComment");
        adminLog.setNote(info.getLrdPath());
        dao.insertAdminLog(adminLog);
    }

    // ------------------------ Getter/Setter Methods

    /**
     * LrdConfigを取得します。
     * @return LrdConfig
     */
    public LrdConfig getConfig() {
        return config;
    }

    /**
     * LrdConfigを設定します。
     * @param config LrdConfig
     */
    public void setConfig(LrdConfig config) {
        this.config = config;
    }

    /**
     * DAOを取得します。
     * @return DAO
     */
    public LrdDao getDao() {
        return dao;
    }

    /**
     * DAOを設定します。
     * @param dao DAO
     */
    public void setDao(LrdDao dao) {
        this.dao = dao;
    }

}
