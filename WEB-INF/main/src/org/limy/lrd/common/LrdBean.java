/*
 * Created 2006/04/21
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
package org.limy.lrd.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.common.util.LrdUtils;

/**
 * Lrdファイルを表すBeanクラスです。
 * @author Naoki Iwami
 */
public class LrdBean implements LinkableTarget {
    
    // ------------------------ Fields
    
    /**
     * Lrdパス情報
     */
    private LrdPath path;
    
    /**
     * ルート要素
     */
    private LrdBlockElement root;
    
    /**
     * タイトル
     */
    private String title;
    
    /**
     * 出力内容にファイルの最終更新日付を含むかどうか
     */
    private boolean showUpdateTime = true;
    
    /**
     * 拡張コードモードをONにするかどうか
     */
    private boolean extCode;
    
    /**
     * スタイルシート一覧
     */
    private Collection<String> stylesheets = new ArrayList<String>();
    
    /**
     * 依存ファイル一覧
     */
    private Collection<String> dependPaths = new ArrayList<String>();

    /**
     * 参照ファイル一覧
     */
    private Collection<String> refs = new ArrayList<String>();
    
    /**
     * 警告文字列
     */
    private StringBuilder alertStrings = new StringBuilder();

    // ------------------------ Constructors

    /**
     * LrdBeanインスタンスを構築します。
     * @param root ルート要素
     * @param path パス情報
     */
    public LrdBean(LrdBlockElement root, LrdPath path) {
        super();
        this.root = root;
        this.path = path;
        refs.add("./");
    }
    
    // ------------------------ Implement Methods

    public LrdProject getProject() {
        return path.getProject();
    }
    
    // ------------------------ Public Methods
    
    /**
     * 警告文字列を返します。
     * @return 警告文字列
     */
    public String getAlertString() {
        return alertStrings.toString();
    }
    
    /**
     * 警告文字列を追加します。
     * @param string 警告文字列
     */
    public void addAlertString(String string) {
        alertStrings.append(string).append("\n");
    }

    /**
     * 参照ファイル一覧を設定します。
     * @param refStr 参照ファイル一覧（カンマ区切り）
     */
    public void setRef(String refStr) {
        StringTokenizer tokenizer = new StringTokenizer(refStr, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            refs.add(token);
        }
    }
    
    /**
     * 参照ファイル情報を返します。
     * @return 依存ファイル情報
     */
    public Collection<String> getRefs() {
        return refs;
    }

    
    /**
     * 依存ファイル情報を返します。
     * @return 依存ファイル情報
     */
    public Collection<String> getDependPaths() {
        return dependPaths;
    }
    
    /**
     * 依存ファイル情報を追加します。
     * @param path 依存ファイルパス
     */
    public void addDependPath(String path) {
        StringTokenizer tokenizer = new StringTokenizer(path, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            dependPaths.add(token);
        }
    }

    /**
     * 全ての動的リンク要素を決定します。
     * @param infos 参照用インスタンス
     */
    public void setAllTargetInfos(Collection<LrdTargetInfo> infos) {
        setNumbering();
        for (LrdElement element : root.getElements()) {
            if (element instanceof DynamicTargetResolver) {
                ((DynamicTargetResolver)element).decideDynamicTarget(this, infos);
            }
        }
    }

    /**
     * スタイルシートファイルを追加します。
     * @param stylesheet スタイルシートファイル
     */
    public void addStylesheet(String stylesheet) {
        stylesheets.add(stylesheet);
    }
    
    /**
     * このLrdファイルにある全ての参照情報を返します。
     * @return 全ての参照情報
     */
    public Collection<LrdTargetInfo> createTargetInfos() {
        setNumbering();
        Collection<LrdTargetInfo> infos = new ArrayList<LrdTargetInfo>();
        
        // 自分自身を参照情報として追加（タイトル）
        new LrdBeanTarget(this).appendThis(this, infos);
        
        for (LrdElement element : root.getElements()) {
            if (element instanceof LrdTargetable) {
                LrdTargetable targetable = (LrdTargetable)element;
                targetable.appendThis(this, infos);
            }
        }
        return infos;
    }
    
    /**
     * ドキュメントに含まれる全てのHTMLへのリンクを抽出して、そのLrdファイルパス一覧を返します。
     * @return ドキュメントに含まれるリンク先Lrdファイルパス一覧
     */
    public Collection<String> createInnerUrls() {
        Collection<String> results = new ArrayList<String>();
        for (LrdElement element : root.getElements()) {
            if (element instanceof LrdParentElement) {
                LrdParentElement multiEl = (LrdParentElement)element;
                for (LrdElement child : multiEl.getElements()) {
                    if (child instanceof LrdLinkElement) {
                        appendLrdFilePath(results, (LrdLinkElement)child);
                    }
                }
            }
        }
        return results;
    }

    /**
     * リンク要素からHTMLへのHREFを見つけたら、対応するLrdファイルパスをresultsに追加します。
     * @param results 結果格納先
     * @param linkEl リンク要素
     */
    private void appendLrdFilePath(Collection<String> results, LrdLinkElement linkEl) {
        if (linkEl.getHref().startsWith("URL:")) {
            String url = linkEl.getHref().substring(4);
            if (url.endsWith(".html")) {
                results.add(LrdUtils.getLrdPath(url)); // subversion.lrd
            }
        }
    }

    // ------------------------ Getter/Setter Methods

    /**
     * タイトルを取得します。
     * @return タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * タイトルを設定します。
     * @param title タイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * ルート要素を取得します。
     * @return ルート要素
     */
    public LrdElement getRoot() {
        return root;
    }
    
    /**
     * Lrdパス情報を取得します。
     * @return Lrdパス情報
     */
    public LrdPath getPath() {
        return path;
    }

    /**
     * 出力内容にファイルの最終更新日付を含むかどうかを取得します。
     * @return 出力内容にファイルの最終更新日付を含むかどうか
     */
    public boolean isShowUpdateTime() {
        return showUpdateTime;
    }

    /**
     * 出力内容にファイルの最終更新日付を含むかどうかを設定します。
     * @param showUpdateTime 出力内容にファイルの最終更新日付を含むかどうか
     */
    public void setShowUpdateTime(boolean showUpdateTime) {
        this.showUpdateTime = showUpdateTime;
    }

    /**
     * 拡張コードモードをONにするかどうかを取得します。
     * @return 拡張コードモードをONにするかどうか
     */
    public boolean isExtCode() {
        return extCode;
    }

    /**
     * 拡張コードモードをONにするかどうかを設定します。
     * @param extCode 拡張コードモードをONにするかどうか
     */
    public void setExtCode(boolean extCode) {
        this.extCode = extCode;
    }

    /**
     * スタイルシート一覧を取得します。
     * @return スタイルシート一覧
     */
    public Collection<String> getStylesheets() {
        return stylesheets;
    }
    
    // ------------------------ Private Methods

    /**
     * 全ての識別子付要素にIDをナンバリングします。
     */
    private void setNumbering() {
        root.setNumberingId("limy:", 1);
    }

}
