/*
 * Created 2007/08/11
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

import org.apache.commons.lang.StringUtils;
import org.limy.lrd.common.RepositoryBean;

/**
 * @author Naoki Iwami
 */
public class UpdateProjectSettingAction extends AbstractAdminAction {

    // ------------------------ Fields

    /** リポジトリID */
    private String repositoryId;

    /** コメント有効フラグ */
    private String enableComment;

    /** リポジトリ文字セット */
    private String repositoryCharset;

    /** デプロイ先文字セット */
    private String viewCharset;

    /** プロジェクト開始年 */
    private int startYear;

    /** 著作者 */
    private String author;

    // ------------------------ Override Methods

    @Override
    protected String doExecuteAdmin() {
        RepositoryBean bean = getModel().getRepositoryBean(repositoryId);
        bean.setEnableComment(!StringUtils.isEmpty(enableComment));
        bean.setRepositoryCharset(repositoryCharset);
        bean.setViewCharset(viewCharset);
        bean.setStartYear(startYear);
        bean.setAuthor(author);
        updateConfigRootBean();
        return SUCCESS;
    }

    // ------------------------ Getter/Setter Methods

    /**
     * リポジトリIDを取得します。
     * @return リポジトリID
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * リポジトリIDを設定します。
     * @param repositoryId リポジトリID
     */
    public void setRepositoryId(String repositoryName) {
        this.repositoryId = repositoryName;
    }

    /**
     * コメント有効フラグを取得します。
     * @return コメント有効フラグ
     */
    public String getEnableComment() {
        return enableComment;
    }

    /**
     * コメント有効フラグを設定します。
     * @param enableComment コメント有効フラグ
     */
    public void setEnableComment(String enableComment) {
        this.enableComment = enableComment;
    }

    /**
     * リポジトリ文字セットを取得します。
     * @return リポジトリ文字セット
     */
    public String getRepositoryCharset() {
        return repositoryCharset;
    }

    /**
     * リポジトリ文字セットを設定します。
     * @param repositoryCharset リポジトリ文字セット
     */
    public void setRepositoryCharset(String repositoryCharset) {
        this.repositoryCharset = repositoryCharset;
    }

    /**
     * デプロイ先文字セットを取得します。
     * @return デプロイ先文字セット
     */
    public String getViewCharset() {
        return viewCharset;
    }

    /**
     * デプロイ先文字セットを設定します。
     * @param viewCharset デプロイ先文字セット
     */
    public void setViewCharset(String viewCharset) {
        this.viewCharset = viewCharset;
    }

    /**
     * プロジェクト開始年を取得します。
     * @return プロジェクト開始年
     */
    public int getStartYear() {
        return startYear;
    }

    /**
     * プロジェクト開始年を設定します。
     * @param startYear プロジェクト開始年
     */
    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    /**
     * 著作者を取得します。
     * @return 著作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 著作者を設定します。
     * @param author 著作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

}
