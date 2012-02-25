/*
 * Created 2006/04/25
 * Copyright (C) 2003-2006  Naoki Iwami (naoki@limy.org)
 *
 * This file is part of limy-portal.
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
package org.limy.lrd.common.bean;

import org.limy.common.util.UrlUtils;


/**
 * Lrdファイルのパス情報を表すクラスです。
 * @author Naoki Iwami
 */
public class LrdPath {

    // ------------------------ Fields
    
    /**
     * Lrdプロジェクト情報
     */
    private LrdProject project;

    /**
     * 基準プロジェクトからの相対パス
     */
    private String relativePath;
    
    // ------------------------ Constructors

    /**
     * LrdPathインスタンスを構築します。
     * @param project Lrdプロジェクト情報
     * @param relativePath 基準プロジェクトからの相対パス
     */
    public LrdPath(LrdProject project, String relativePath) {
        super();
        this.project = project;
        this.relativePath = relativePath;
    }
    
    // ------------------------ Override Methods

    @Override
    public String toString() {
        return relativePath;
    }
    
    // ------------------------ Public Methods

    public int getDepth() {
        if ("/".equals(relativePath)) {
            return 0;
        }
        
        int pos = 0;
        int count = 1;
        while (true) {
            pos = relativePath.indexOf('/', pos) + 1;
            if (pos == 0) {
                break;
            }
            ++count;
        }
        return count;
    }
    
    /**
     * パスの最終ノード名を返します。
     * @return
     */
    public String getName() {
        int index = relativePath.lastIndexOf('/');
        if (index >= 0) {
            return relativePath.substring(index + 1);
        }
        return relativePath;
    }

    /**
     * パスの最終ノードを除いた名称を返します。
     * <p>
     * ノードが一つの場合は、空文字を返します。
     * </p>
     * @return
     */
    public String getBaseName() {
        int index = relativePath.lastIndexOf('/');
        if (index >= 0) {
            return relativePath.substring(0, index);
        }
        return "";
    }

    /**
     * リポジトリURLを取得します。
     * @return SVN形式のURL
     */
    public String getRepositoryUrl() {
        if (project.getRepositoryInfo() == null) {
            return relativePath;
        }
        String baseUrl = project.getRepositoryInfo().getRepositoryUrl();
        return UrlUtils.concatUrl(baseUrl, relativePath);
//        return baseUrl + relativePath;
    }
    
    /**
     * このパスを基準とした新しいパスを生成して返します。
     * @param name 新しいパス名
     * @return 新しいパス
     */
    public LrdPath createNewPath(String name) {
        String path = relativePath;
        if (path.length() == 0) {
            path = name;
        } else if (!path.endsWith("/")) {
            path += "/" + name;
        } else {
            path += name;
        }
        return new LrdPath(project, path);
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * Lrdプロジェクト情報を取得します。
     * @return Lrdプロジェクト情報
     */
    public LrdProject getProject() {
        return project;
    }

    /**
     * 基準プロジェクトからの相対パスを取得します。
     * @return 基準プロジェクトからの相対パス
     */
    public String getRelativePath() {
        return relativePath;
    }


}
