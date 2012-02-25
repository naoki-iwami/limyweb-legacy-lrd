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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.UrlUtils;
import org.limy.lrd.TargetInfoCreator;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdPath;

/**
 * Lrd参照情報を生成します。
 * @author Naoki Iwami
 */
public class TargetInfoCreatorImpl implements TargetInfoCreator {
    
    // ------------------------ Fields

    /** キャッシュマネージャ */
    private LrdCacheManager cacheManager;
    
    // ------------------------ Getter/Setter Methods

    /**
     * キャッシュマネージャを設定します。
     * @param cacheManager キャッシュマネージャ
     */
    public void setCacheManager(LrdCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // ------------------------ Implement Methods

    /**
     * 参照情報を作成して返します。
     * @param repositoryBean リポジトリBran
     * @param bean LrdBean
     * @return 参照情報
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    public Collection<LrdTargetInfo> createTargetInfos(
            RepositoryBean repositoryBean,
            LrdBean bean)
            throws LrdException, IOException {

        LrdRepository repository = repositoryBean.getRepository();
        
        String lrdPath = bean.getPath().getRelativePath();
        
        // 参照情報を作成する
        Collection<LrdTargetInfo> extInfos = new ArrayList<LrdTargetInfo>();
        List<String> refFiles = new ArrayList<String>();
        for (String ref : bean.getRefs()) {
            String targetUrl = UrlUtils.concatUrl(UrlUtils.getParent(lrdPath), ref);
            
            if (repository.isDirectory(targetUrl)) {
                LrdPath[] files = repository.getFilesFromDirectory(targetUrl);
                refFiles.addAll(Arrays.asList(getPathStrs(files)));
            } else {
                refFiles.add(targetUrl);
            }
        }
        refFiles.remove(lrdPath); // 自Bean情報は参照情報から削除する
        CommitFileInfo[] infos = repository.getRepositoryFiles(
                refFiles.toArray(new String[refFiles.size()]));
        for (CommitFileInfo info : infos) {
            appendExtInfos(repositoryBean.getId(), info, extInfos);
        }
        
        // 今回のコンテンツはLrdBeanから参照情報作成
        extInfos.addAll(bean.createTargetInfos());
        return extInfos;
    }

    // ------------------------ Private Methods

    /**
     * Lrdパス一覧から相対パス文字列一覧を生成します。
     * @param lrdPaths Lrdパス一覧
     * @return 相対パス文字列一覧
     */
    private String[] getPathStrs(LrdPath[] lrdPaths) {
        String[] results = new String[lrdPaths.length];
        for (int i = 0; i < lrdPaths.length; i++) {
            results[i] = lrdPaths[i].getRelativePath();
        }
        return results;
    }

    /**
     * Lrdファイルを解析して参照情報をextInfosに追加します。
     * @param repositoryId リポジトリID
     * @param info Lrdコンテンツ内容
     * @param extInfos リンク先情報
     * @throws IOException I/O例外
     */
    private void appendExtInfos(String repositoryId,
            CommitFileInfo info, Collection<LrdTargetInfo> extInfos)
            throws IOException {
        
        // コンテンツ内容を取得
        String contents = info.getContentStr();
        BufferedReader reader = new BufferedReader(new StringReader(contents));
        // コンテンツ内容からLrdBeanを生成
        LrdBean bean = cacheManager.getBean(repositoryId, info);
        // LrdBeanに含まれる全ての参照情報をextInfosに追加
        extInfos.addAll(bean.createTargetInfos());
        reader.close();
    }
    
}
