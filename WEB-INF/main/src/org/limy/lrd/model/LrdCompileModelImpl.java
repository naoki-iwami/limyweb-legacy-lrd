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

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.runtime.RuntimeInstance;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.LrdCompileModel;
import org.limy.lrd.LrdConfig;
import org.limy.lrd.TargetInfoCreator;
import org.limy.lrd.bean.LrdLightBean;
import org.limy.lrd.bean.RecentEntry;
import org.limy.lrd.bean.RecentEntryChild;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdDwrResult;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.deploy.DynamicResourceLoader;

/**
 * コンパイル関連のモデル実装クラスです。
 * @author Naoki Iwami
 */
public class LrdCompileModelImpl implements LrdCompileModel {

    // ------------------------ Fields (model)
    
    /** LrdConfig */
    private LrdConfig config;

    /** キャッシュマネージャ */
    private LrdCacheManager cacheManager;

    /** 参照情報作成担当 */
    private TargetInfoCreator infoCreator;
    
    /** Lrd Writer */
    private LrdWriter lrdWriter;
    
    // ------------------------ Implement Methods

    /**
     * LrdファイルをコンパイルしてHTML形式で返します。
     * @param repositoryId リポジトリID
     * @param lrdPath パス情報
     * @param contents Lrdコンテンツ内容
     * @return HTML形式文字列
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public LrdDwrResult compile(
            String repositoryId, String lrdPath, String contents)
            throws IOException, LrdException {
       
        LrdBean bean = cacheManager.createBean(repositoryId, lrdPath, contents, false);
        RepositoryBean repositoryBean = config.getRepositoryBean(repositoryId);
        
        StringWriter buff = new StringWriter();

        Collection<LrdTargetInfo> extInfos = infoCreator.createTargetInfos(
                repositoryBean, bean);

        LrdNode menuRoot = createMenuTree(repositoryId, bean);
        lrdWriter.writeSimpleHtml(repositoryBean, bean, buff, extInfos, menuRoot);
        
        LrdDwrResult result = new LrdDwrResult(buff.toString());
        result.createAdminLinks(lrdPath);
        if (!StringUtils.isEmpty(bean.getAlertString())) {
            result.setAlertString(bean.getAlertString());
        }
        return result;
    }

    public LrdDwrResult compileWithTemplate(String repositoryId,
            String contents, String templateString)
            throws IOException, LrdException {
        
        LrdBean bean = cacheManager.createDummyBean(repositoryId, contents);
        RepositoryBean repositoryBean = config.getRepositoryBean(repositoryId);
        
        StringWriter buff = new StringWriter();

        LrdNode menuRoot = createMenuTree(repositoryId, bean);
        Template template = new Template();
        template.setResourceLoader(new DynamicResourceLoader(templateString));
        template.setRuntimeServices(new RuntimeInstance());
        template.setName(".dummy");
        template.process();
        lrdWriter.writeHtml(repositoryBean, bean, buff, menuRoot, template);
        
        return new LrdDwrResult(buff.toString());
    }
    
    /**
     * LrdBeanに関連するメニュー要素ツリーを作成して返します。
     * @param repositoryId リポジトリID
     * @param bean LrdBean
     * @return メニュー要素ツリー
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    public LrdNode createMenuTree(String repositoryId, LrdBean bean) throws LrdException,
            IOException {

        return cacheManager.getMenuTree(repositoryId, bean);
    }

    /**
     * 全メニューをHTML形式で取得します。
     * @param repositoryId リポジトリID
     * @return 全メニュー
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    public String getAllMenu(String repositoryId) throws LrdException, IOException {
        RepositoryBean repositoryBean = config.getRepositoryBean(repositoryId);
        return repositoryBean.getRepository().getAllMenu(lrdWriter);
    }


    public String getRecentEntryHtml(String repositoryId, String deployId)
            throws LrdException, IOException {
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

        RepositoryBean repositoryBean = config.getRepositoryBean(repositoryId);
        Collection<CommitFileInfo> files = repositoryBean.getRepository().getRecentUpdateFiles(10);
        Set<String> dates = new HashSet<String>();
        
        Map<String, String> titles = new HashMap<String, String>();
        for (CommitFileInfo info : files) {
            dates.add(format.format(info.getCommittedDate()));
            LrdBean bean = cacheManager.getBean(repositoryId, info);
            titles.put(info.getPath(), bean.getTitle());
        }
        String[] dateAry = dates.toArray(new String[dates.size()]);
        Arrays.sort(dateAry);
        
        Collection<RecentEntry> results = new ArrayList<RecentEntry>();
        for (int i = dateAry.length - 1; i >= 0; i--) {
            String dateStr = dateAry[i];
            RecentEntry entry = new RecentEntry();
            try {
                entry.setDate(format.parse(dateStr));
            } catch (ParseException e) {
                // ignore
            }
            for (CommitFileInfo info : files) {
                if (format.format(info.getCommittedDate()).equals(dateStr)) {
                    RecentEntryChild child = new RecentEntryChild();
                    child.setPath(info.getPath());
                    child.setTitle(titles.get(info.getPath()));
                    entry.addSubEntry(child);
                }
            }
            results.add(entry);
        }
        
        StringWriter out = new StringWriter();
        LrdDeployInfo deployInfo = null;
        for (LrdDeployInfo deploy : repositoryBean.getDeployInfos()) {
            if (deploy.getId().equals(deployId)) {
                deployInfo = deploy;
            }
        }
        lrdWriter.writeRecentPage(repositoryBean, deployInfo, out, results);
        return out.toString();
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
     * キャッシュマネージャを取得します。
     * @return キャッシュマネージャ
     */
    public LrdCacheManager getCacheManager() {
        return cacheManager;
    }

    /**
     * キャッシュマネージャを設定します。
     * @param cacheManager キャッシュマネージャ
     */
    public void setCacheManager(LrdCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 参照情報作成担当を取得します。
     * @return 参照情報作成担当
     */
    public TargetInfoCreator getInfoCreator() {
        return infoCreator;
    }

    /**
     * 参照情報作成担当を設定します。
     * @param infoCreator 参照情報作成担当
     */
    public void setInfoCreator(TargetInfoCreator infoCreator) {
        this.infoCreator = infoCreator;
    }

    /**
     * Lrd Writerを取得します。
     * @return Lrd Writer
     */
    public LrdWriter getLrdWriter() {
        return lrdWriter;
    }

    /**
     * Lrd Writerを設定します。
     * @param lrdWriter Lrd Writer
     */
    public void setLrdWriter(LrdWriter lrdWriter) {
        this.lrdWriter = lrdWriter;
    }


    
}
