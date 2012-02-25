/*
 * Created 2007/07/28
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.LimyException;
import org.limy.common.web.SpamChecker;
import org.limy.common.web.WebworkUtils;
import org.limy.lrd.common.bean.LrdCommentInfo;
import org.limy.lrd.common.bean.LrdDeployInfo;

import com.opensymphony.webwork.ServletActionContext;

/**
 * Lrdのページにコメントを記述するためのアクションクラスです。
 * @author Naoki Iwami
 */
public class LrdCommentAction extends BaseLrdAction {

    /** serialVersionUID */
    private static final long serialVersionUID = -2913553740730393742L;

    /** logger */
    private static final Log LOG = LogFactory.getLog(LrdCommentAction.class);
    
    // ------------------------ Fields (model)

    /** DBモデル */
    private LrdDbModel dbModel;
    
    // ------------------------ Fields (input)

    /** リポジトリ名 */
    private String repositoryName;
    
    /** Lrdパス */
    private String lrdPath;

    /** 送信フラグ */
    private boolean submit;
    
    /** 送信者名 */
    private String resName;
    
    /** コメント本文 */
    private String resText;
    
    // ------------------------ Fields (output)

    /** エラーメッセージ */
    private String errorMessage;

    // ------------------------ Implement Methods

    @Override
    protected String doExecute() {
        
        LOG.info("start doExecute");

        if (submit) {
            
            try {
                checkMessage();
            } catch (LimyException e) {
                errorMessage = e.getMessage();
                return ERROR;
            }
            
            LrdCommentInfo info = new LrdCommentInfo();
            info.setLrdPath(lrdPath);
            info.setUserName(resName);
            info.setMainText(resText);
            info.setIpAddress(WebworkUtils.getRemoteAddr());
            try {
                dbModel.submitLrdComment(repositoryName, info);
                String deployId = decideDeployId();
                getModel().deploy(repositoryName, lrdPath, deployId);
            } catch (LimyException e) {
                errorMessage = e.getMessage();
                return ERROR;
            } catch (IOException e) {
                errorMessage = e.getMessage();
                return ERROR;
            }
        }
        
        LOG.info("end doExecute");

        return SUCCESS;
    }

    // ------------------------ Bind Setter Methods (framework)
    
    /**
     * DBモデルを設定します。
     * @param dbModel DBモデル
     */
    public void setDbModel(LrdDbModel dbModel) {
        this.dbModel = dbModel;
    }

    // ------------------------ Web Getter Methods
    
    public String getRedirectUrl() {
        return ServletActionContext.getRequest().getHeader("referer");
    }
    
    /**
     * エラーメッセージを取得します。
     * @return エラーメッセージ
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    // ------------------------ Web Setter Methods

    /**
     * リポジトリ名を設定します。
     * @param repositoryName リポジトリ名
     */
    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    /**
     * 送信者名を設定します。
     * @param resName 送信者名
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * コメント本文を設定します。
     * @param resText コメント本文
     */
    public void setResText(String resText) {
        this.resText = resText;
    }

    /**
     * Lrdパスを設定します。
     * @param lrdPath Lrdパス
     */
    public void setLrdPath(String lrdPath) {
        this.lrdPath = lrdPath;
    }

    /**
     * 送信フラグを設定します。
     * @param submit 送信フラグ
     */
    public void setSubmit(String submit) {
        this.submit = submit != null;
    }

    // ------------------------ Private Methods

    /**
     * コメントのメッセージをチェックします。
     * @throws LimyException 不正なメッセージの場合
     */
    private void checkMessage() throws LimyException {
        SpamChecker.checkNotEmpty(resName);
        SpamChecker.checkJapanese(resText);
        SpamChecker.checkKeyword(resText, "http:", "url=");
    }
    
    private String decideDeployId() throws LimyException {
        
        String url = getRedirectUrl();
        for (LrdDeployInfo info : getModel().getRepositoryBean(repositoryName).getDeployInfos()) {
            if (url.startsWith(info.getDeployUrl())) {
                return info.getId();
            }
        }
        throw new LimyException("デプロイ先が決定しません。 : " + url);
    }


}
