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
package org.limy.lrd;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.UrlUtils;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;

/**
 * 添付ファイルをアップロードするアクションクラスです。
 * @author Naoki Iwami
 */
public class UploadFileAction extends BaseLrdAction {

    /** logger */
    private static final Log LOG = LogFactory.getLog(UploadFileAction.class);
    
    // ------------------------ Fields

    /** リポジトリID */
    private String id;
    
    /** Lrdパス */
    private String lrdPath;
    
    /** アップロードファイル */
    private File uploadFile;

    /** コンテンツタイプ */
    private String uploadFileContentType;
    
    /** ファイル名 */
    private String uploadFileFileName;

    // ------------------------ Override Methods

    @Override
    protected String doExecute() {
        if (uploadFile != null) {
            LrdRepository repository = getModel().getRepository(id);
            String url = UrlUtils.concatUrl(UrlUtils.getParent(lrdPath), uploadFileFileName);
            try {
                repository.addAndcommitMultiFile(new CommitFileInfo[] {
                        new CommitFileInfo(url, FileUtils.readFileToByteArray(uploadFile), null),
                });
            } catch (LrdException e) {
                LOG.error(e.getMessage(), e);
                return ERROR;
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
                return ERROR;
            }
            
        }
        return SUCCESS;
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

    /**
     * アップロードファイルを取得します。
     * @return アップロードファイル
     */
    public File getUploadFile() {
        return uploadFile;
    }

    /**
     * アップロードファイルを設定します。
     * @param uploadFile アップロードファイル
     */
    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    /**
     * コンテンツタイプを取得します。
     * @return コンテンツタイプ
     */
    public String getUploadFileContentType() {
        return uploadFileContentType;
    }

    /**
     * コンテンツタイプを設定します。
     * @param uploadFileContentType コンテンツタイプ
     */
    public void setUploadFileContentType(String uploadFileContentType) {
        this.uploadFileContentType = uploadFileContentType;
    }

    /**
     * ファイル名を取得します。
     * @return ファイル名
     */
    public String getUploadFileFileName() {
        return uploadFileFileName;
    }

    /**
     * ファイル名を設定します。
     * @param uploadFileFileName ファイル名
     */
    public void setUploadFileFileName(String uploadFileFileName) {
        this.uploadFileFileName = uploadFileFileName;
    }


}
