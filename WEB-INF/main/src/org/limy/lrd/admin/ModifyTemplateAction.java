/*
 * Created 2007/08/18
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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.HtmlUtils;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.VelocityEngineModel;

/**
 * @author Naoki Iwami
 */
public class ModifyTemplateAction extends AbstractAdminAction {

    // ------------------------ Constants

    /** logger */
    private static final Log LOG = LogFactory.getLog(ModifyTemplateAction.class);
    
    // ------------------------ Fields (model)

    /** Velocityエンジンモデル */
    private VelocityEngineModel engineModel;

    // ------------------------ Fields

    /** リポジトリID */
    private String repositoryId;

    /** 編集するファイル名 */
    private String file;
    
    /** 更新するコンテンツ内容 */
    private String templateContents;

    // ------------------------ Override Methods

    @Override
    protected String doExecuteAdmin() {
        if (templateContents != null) {
            return doSave();
        }
        if (file != null) {
            return "success_file";
        }
        return SUCCESS;
    }

    // ------------------------ Bind Setter Methods (framework)

    /**
     * Velocityエンジンモデルを設定します。
     * @param engineModel Velocityエンジンモデル
     */
    public void setVelocityEngineModel(VelocityEngineModel engineModel) {
        this.engineModel = engineModel;
    }

    // ------------------------ Public Methods

    public void setTemplateContents(String contents) {
        this.templateContents = contents;
    }
    
    public String getTemplateContents() throws IOException {
        String str = FileUtils.readFileToString(getTemplateFile(), "UTF-8");
        return HtmlUtils.quoteHtml(str);
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
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * 編集するファイル名を取得します。
     * @return 編集するファイル名
     */
    public String getFile() {
        return file;
    }

    /**
     * 編集するファイル名を設定します。
     * @param file 編集するファイル名
     */
    public void setFile(String file) {
        this.file = file;
    }

    // ------------------------ Private Methods

    private File getTemplateFile() throws IOException {
        return new File(getModel().getRepositoryBean(repositoryId).getLocalContentFile(),
                ".template/" + file);
    }

    private String doSave() {
        engineModel.refreshEngine(getModel().getRepositoryBean(repositoryId));
        try {
            FileUtils.writeStringToFile(getTemplateFile(), templateContents, "UTF-8");
            LrdRepository repository = getModel().getRepository(repositoryId);
            repository.commitMultiFile(new CommitFileInfo[] {
                    new CommitFileInfo(".template/" + file, templateContents.getBytes("UTF-8"), null),
            });
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return ERROR;
        } catch (LrdException e) {
            LOG.error(e.getMessage(), e);
            return ERROR;
        }
        return "success_file";
    }

}
