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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limy.common.repository.RepositoryInfo;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.Formatters;
import org.limy.common.util.UrlUtils;
import org.limy.lrd.common.util.LrdUtils;


/**
 * リンク要素を表します。
 * @author Naoki Iwami
 */
public class LrdLinkElement extends LrdTextElement implements DynamicTargetResolver {
    
    // ------------------------ Constants

    /** log */
    private static final Log LOG = LogFactory.getLog(LrdLinkElement.class);
    
    // ------------------------ Fields

    /** リポジトリ */
    private final LrdRepository repository;
    
    /**
     * リンク先
     */
    private String href;
    
    /**
     * 動的に決定したリンク先
     */
    private String dynamicHref;
    
    /**
     * リンク先リソースの特定名
     */
    private String specifyTarget;
    
    /**
     * リンク先を決定したかどうか
     */
    private boolean successLink;
    
    /**
     * リンク先の更新日付を表示するかどうか
     */
    private boolean showDate;
    
    // ------------------------ Constructors

    /**
     * LrdLinkElementインスタンスを構築します。
     * @param repository リポジトリ
     * @param parent 親要素
     * @param text リンク先文字列
     */
    public LrdLinkElement(LrdRepository repository, LrdElement parent, String text) {
        super(parent, Type.LINK, text);
        this.repository = repository;
        this.href = text;
        if (text.startsWith("\"")) {
            // ダブルクォーテーションで囲まれていたら外す
            this.href = text.substring(1, text.length() - 1);
            setText(this.href);
        }
        if (text.startsWith("URL:") || text.startsWith("IMG:")) {
            setText(text.substring(4));
        }
    }

    /**
     * LrdLinkElementインスタンスを構築します。
     * @param repository リポジトリ
     * @param parent 親要素
     * @param href リンク先HREF
     * @param text 表示文字列
     */
    public LrdLinkElement(LrdRepository repository, LrdElement parent, String href, String text) {
        super(parent, Type.LINK, text);
        this.repository = repository;
        if (href.startsWith("\"")) {
            // ダブルクォーテーションで囲まれていたら外す
            this.href  = href.substring(1, href.length() - 1);
        } else {
            this.href  = href;
        }
    }
    
    // ------------------------ Implement Methods
    
    /**
     * 動的リンク情報を決定します。
     * @param bean Lrdインスタンス
     * @param infos 
     */
    public void decideDynamicTarget(LrdBean bean, Collection<LrdTargetInfo> infos) {
        if (!isDynamicLink()) {
            return;
        }
        
        successLink = false;
        
        String baseSvnUrl = UrlUtils.getParent(bean.getPath().getRepositoryUrl());
        
        // 複数リンク先が見つかった場合、それを格納する場所
        Collection<LrdTargetInfo> matchedTargets = new ArrayList<LrdTargetInfo>();
        
        // 最も適切なリンク先
        LrdTargetInfo bestTarget = decideBestTarget(infos, matchedTargets);
        
        // 動的リンク先を決定
        decideDynamicHref(baseSvnUrl, matchedTargets, bestTarget);
        
    }

    // ------------------------ Public Methods

    /**
     * HTML表示文字列を返します。
     * @return HTML表示文字列
     */
    public String getLinkText() {
        String result;
        if (!isDynamicLink()) {
            // 静的リンク
            if (href.startsWith("IMG:")) {
                // イメージ
                result = "<img src=\"" + href.substring(4) + "\" alt=\"" + getRawText() + "\" />";
            } else {
                // 通常の静的リンク
                result = "<a href=\"" + getUrl() + "\">" + getRawText() + "</a>";
                if (getUrl().startsWith("http://")) {
                    // 外部リンク
                    result = "<a class=\"externalLink\" title=\"External Link\" href=\""
                        + getUrl() + "\">" + getRawText() + "</a>";
                }
                
                if (showDate) {
                    try {
                        String relativePath = UrlUtils.concatUrl(
                                UrlUtils.getParent(getBean().getPath().getRelativePath()),
                                getUrl());
                        relativePath = LrdUtils.getLrdPath(relativePath);
                        
                        RepositoryInfo info = getBean().getProject().getRepositoryInfo();
                        Date date = repository.getTimestamp(
                                relativePath, info.getAuthUser(), info.getAuthPass());
                        
                        CommitFileInfo commitFileInfo = repository.getRepositoryFile(relativePath);
                        String lines = commitFileInfo.getContentStr();
                        String[] split = lines.split("\\n");
                        for (String line : split) {
                            if (line.startsWith("date=")) {
                                date = new SimpleDateFormat("yyyy/MM/dd").parse(line.substring(5));
                            }
                            if (line.startsWith("=begin")) {
                                break;
                            }
                        }
                        
                        result += "　<font size='-1'>("
                            + Formatters.dateFormat(Formatters.D_VIEW_8, date)
                            + ")</font>";
                        
                    } catch (LrdException e) {
                        // ファイルが存在しない場合
                        result += "　<font color=\"red\">Not Found!</font>";
                    } catch (ParseException e) {
                        // ignore
                    }
                }
            }
        } else {
            // 動的リンク
            if (successLink) {
                result = "<a href=\"" + dynamicHref + "\">" + getRawText() + "</a>";
            } else {
                // リンク先が見つからなかった場合
                result = "<strong>!!" + getRawText() + "!!</strong>";
                getBean().addAlertString("リンク : " + getRawText() + "が見つかりません。");
                LOG.warn(getBean().getPath().getRelativePath()
                        + " : " + getBean().getAlertString());
            }
        }
        return result;
    }

    // ------------------------ Getter/Setter Methods
    
    /**
     * リンク先を取得します。
     * @return リンク先
     */
    public String getHref() {
        return href;
    }

    /**
     * リンク先を設定します。
     * @param href リンク先
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * リンク先リソースの特定名を取得します。
     * @return リンク先リソースの特定名
     */
    public String getSpecifyTarget() {
        return specifyTarget;
    }

    /**
     * リンク先リソースの特定名を設定します。
     * @param specifyTarget リンク先リソースの特定名
     */
    public void setSpecifyTarget(String specifyTarget) {
        this.specifyTarget = specifyTarget;
    }

    /**
     * リンク先を決定したかどうかを取得します。
     * @return リンク先を決定したかどうか
     */
    public boolean isSuccessLink() {
        return successLink;
    }

    /**
     * リンク先を決定したかどうかを設定します。
     * @param successLink リンク先を決定したかどうか
     */
    public void setSuccessLink(boolean successLink) {
        this.successLink = successLink;
    }

    /**
     * リンク先の更新日付を表示するかどうかを取得します。
     * @return リンク先の更新日付を表示するかどうか
     */
    public boolean isShowDate() {
        return showDate;
    }

    /**
     * リンク先の更新日付を表示するかどうかを設定します。
     * @param showDate リンク先の更新日付を表示するかどうか
     */
    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }
    
    // ------------------------ Private Methods

    /**
     * 動的にリンク先を決定するかどうかを取得します。
     * @return 動的にリンク先を決定するかどうか
     */
    public boolean isDynamicLink() {
        if (href == null) {
            return false;
        }
        if (href.startsWith("URL:")) {
            return false;
        }
        if (href.startsWith("IMG:")) {
            return false;
        }
        return true;
    }

    /**
     * リンク先URLを返します。
     * @return リンク先URL
     */
    private String getUrl() {
        if (href == null) {
            return "";
        }
        if (href.startsWith("URL:")) {
            return href.substring(4);
        }
        return href;
    }

    /**
     * リンク先を動的に決定します。
     * @param baseSvnUrl 基準URL
     * @param availableInfos 利用可能な参照要素一覧
     * @param bestTarget 一番マッチした参照要素
     */
    private void decideDynamicHref(String baseSvnUrl,
            Collection<LrdTargetInfo> availableInfos, LrdTargetInfo bestTarget) {
        
        LrdTargetInfo resultTarget = getBestLinkTarget(availableInfos, bestTarget);
        
        if (resultTarget != null) {
            // リンク先が見つかった場合
            String linkSvnUrl = resultTarget.getTarget().getPath().getRepositoryUrl();
            linkSvnUrl = LrdUtils.getHtmlPath(linkSvnUrl);
            String relativePath = UrlUtils.getRelativeUrl(baseSvnUrl, linkSvnUrl);
            
            dynamicHref = relativePath;
            if (resultTarget.getTargetNo() != null) {
                dynamicHref += "#" + resultTarget.getTargetNo();
            }
            successLink = true;
        } else {
            // リンク先が見つからなかった場合
            LOG.warn("Link " + href + " is not found.");
        }
    }

    /**
     * 状況に応じて一番適切なリンク先を探して返します。
     * @param matchedTargets 利用可能な参照要素一覧
     * @param bestTarget 一番マッチした参照要素
     * @return 一番適切なリンク先
     */
    private LrdTargetInfo getBestLinkTarget(
            Collection<LrdTargetInfo> matchedTargets,
            LrdTargetInfo bestTarget) {
        
        LrdTargetInfo result = null;

        if (specifyTarget != null) {
            // リンク先リソースが特定されている場合は、そのリソースへのリンクを強制的に使用
            result = bestTarget;
        } else {
            // リンク先リソースが指定されていない場合
            if (bestTarget != null) {
                // 最適なリンク候補がある場合、それを使用
                result = bestTarget;
            } else {
                // 最適なリンク候補が無い場合は、一覧から最初の一つを選択
                if (matchedTargets.iterator().hasNext()) {
                    result = matchedTargets.iterator().next();
                }
            }
        }
        return result;
    }
    
    /**
     * 最も適切なリンク先を決定します。
     * @param refInfos 全ての参照先情報
     * @param matchedTargets リンク先として利用できる参照先を格納するコレクション
     * @return 最も適切なリンク先（決定しなかったらnull）
     */
    private LrdTargetInfo decideBestTarget(
            Collection<LrdTargetInfo> refInfos,
            Collection<LrdTargetInfo> matchedTargets) {
        
        LrdTargetInfo result = null;

        // 最適なURLを決定
        String bestTargetUrl = getBestTargetSvnUrl();

        for (LrdTargetInfo info : refInfos) { // 全参照先をループ
            if (info.getText().equals(href)) { // 参照文字列が一致した場合
                matchedTargets.add(info); // 利用可能なコレクションに追加
                if (result == null
                        && info.getTarget().getPath().getRelativePath().endsWith(bestTargetUrl)) {
                    // 参照先URLが最適なものだったら、この参照を最適として判断
                    result = info;
                }
            }
        }
        return result;
    }

    /**
     * 動的決定時に最適だと思われるURLを返します。
     * @return 最適だと思われるURL
     */
    private String getBestTargetSvnUrl() {
        
        String searchTarget;
        if (specifyTarget != null) {
            // リンク先が特定されている場合、それを使用
            searchTarget = specifyTarget.substring(
                    0, specifyTarget.length() - 5) + ".lrd";
        } else {
            // リンク先が特定されていない場合、自身のパスを使用
            searchTarget = getBean().getPath().getRelativePath();
        }
        return searchTarget;
    }

}
