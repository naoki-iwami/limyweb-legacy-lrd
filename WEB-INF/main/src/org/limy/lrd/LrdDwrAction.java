/*
 * Created 2007/08/19
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

import org.directwebremoting.annotations.DataTransferObject;
import org.limy.common.web.WebDwrUtils;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.bean.LrdDwrResult;

/**
 * @author Naoki Iwami
 */
@DataTransferObject
public class LrdDwrAction {
    
    // ------------------------ Fields
    
    /** Lrdマルチモデル */
    private LrdMultiModel model;

    /** コンパイルモデル */
    private LrdCompileModel compileModel;

    /** DBモデル */
    private LrdDbModel dbModel;
    
    /** リポジトリID */
    private String repositoryId;
    
    /** Lrdパス */
    private String lrdPath;
    
    /** 呼び出しメソッド名 */
    private String methodName;
    
    /** コンテンツ内容 */
    private String contents;
    
    /** 結果 */
    private LrdDwrResult innerResult;

    public final String execute() {
        return doExecute();
    }

    // ------------------------ Private Methods

    private String doExecute() {
        try {
            if ("changeLocation".equals(methodName)) {
                innerResult = getModel().changeLocation(repositoryId, lrdPath);
            }
            if ("compile".equals(methodName)) {
                innerResult = getCompileModel().compile(repositoryId, lrdPath, contents);
            }
            if ("commit".equals(methodName)) {
                innerResult = getModel().commit(repositoryId, lrdPath, contents);
                innerResult.setAlertString("コミットが完了しました。");
            }
            if ("deployFrame".equals(methodName)) {
                innerResult = new LrdDwrResult();
                getModel().deploy(repositoryId, lrdPath, contents);
                innerResult.setAlertString("デプロイが成功しました。");
            }
            if ("deployFull".equals(methodName)) {
                innerResult = new LrdDwrResult();
                getModel().deployAll(repositoryId, contents, WebDwrUtils.createWebSession());
                innerResult.setAlertString("デプロイが成功しました。");
            }
            if ("deployRecentPage".equals(methodName)) {
                innerResult = new LrdDwrResult();
                getModel().deployRecentPage(repositoryId, contents);
                innerResult.setAlertString("最新更新ページをアップロードしました。");
            }
            if ("addRss".equals(methodName)) {
                innerResult = new LrdDwrResult();
                getDbModel().addRss(repositoryId, lrdPath);
                innerResult.setAlertString("RSSにこのページを追加しました。");
            }
            if ("getAllMenu".equals(methodName)) {
                innerResult = new LrdDwrResult();
                innerResult.setMainString(getCompileModel().getAllMenu(repositoryId));
            }
            if ("createFile".equals(methodName)) {
                
                innerResult = new LrdDwrResult();
                try {
                    getModel().createFile(repositoryId, contents);
                    innerResult.setAlertString(lrdPath + " を作成しました。");
                } catch (LrdException e) {
                    innerResult.setError(true);
                    innerResult.setAlertString(lrdPath
                            + " を作成中にエラーが発生しました。" + e.getMessage());
                }
            }
            if ("templatePreview".equals(methodName)) {
                innerResult = getCompileModel().compileWithTemplate(
                        repositoryId, createDummyContents(), contents);
            }
        } catch (LrdException e) {
            innerResult = new LrdDwrResult();
            innerResult.setAlertString(e.getMessage());
            innerResult.setError(true);
        } catch (IOException e) {
            innerResult = new LrdDwrResult();
            innerResult.setAlertString(e.getMessage());
            innerResult.setError(true);
        }
        return "success";
    }

    
    // ------------------------ Getter/Setter Methods

    /**
     * リポジトリIDを設定します。
     * @param repositoryId リポジトリID
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * Lrdマルチモデルを取得します。
     * @return Lrdマルチモデル
     */
    private LrdMultiModel getModel() {
        return model;
    }

    /**
     * Lrdマルチモデルを設定します。
     * @param model Lrdマルチモデル
     */
    public void setModel(LrdMultiModel model) {
        this.model = model;
    }

    /**
     * コンパイルモデルを取得します。
     * @return コンパイルモデル
     */
    private LrdCompileModel getCompileModel() {
        return compileModel;
    }

    /**
     * コンパイルモデルを設定します。
     * @param compileModel コンパイルモデル
     */
    public void setCompileModel(LrdCompileModel compileModel) {
        this.compileModel = compileModel;
    }

    /**
     * DBモデルを取得します。
     * @return DBモデル
     */
    private LrdDbModel getDbModel() {
        return dbModel;
    }

    /**
     * DBモデルを設定します。
     * @param dbModel DBモデル
     */
    public void setDbModel(LrdDbModel dbModel) {
        this.dbModel = dbModel;
    }

    /**
     * リポジトリIDを取得します。
     * @return リポジトリID
     */
    public String getRepositoryId() {
        return repositoryId;
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
     * 呼び出しメソッド名を取得します。
     * @return 呼び出しメソッド名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 呼び出しメソッド名を設定します。
     * @param methodName 呼び出しメソッド名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    // ------------------------ Delegate Methods

    public String getAlertString() {
        return innerResult.getAlertString();
    }

    public String getMainString() {
        return innerResult.getMainString();
    }

    public boolean isError() {
        return innerResult.isError();
    }

    /**
     * コンテンツ内容を取得します。
     * @return コンテンツ内容
     */
    public String getContents() {
        return contents;
    }

    /**
     * コンテンツ内容を設定します。
     * @param contents コンテンツ内容
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * 結果を取得します。
     * @return 結果
     */
    public LrdDwrResult getInnerResult() {
        return innerResult;
    }

    /**
     * 結果を設定します。
     * @param innerResult 結果
     */
    public void setInnerResult(LrdDwrResult innerResult) {
        this.innerResult = innerResult;
    }

    // ------------------------ Private Methods


    private String createDummyContents() {
        return "=begin\nok.\n=end";
    }

}
