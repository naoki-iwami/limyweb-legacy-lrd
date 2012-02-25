/*
 * Created 2007/01/24
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
package org.limy.lrd.writer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.limy.common.repository.RepositoryInfo;
import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.Formatters;
import org.limy.lrd.LrdMultiModel;
import org.limy.lrd.LrdVelocityMacro;
import org.limy.lrd.bean.LrdLightBean;
import org.limy.lrd.bean.RecentEntry;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.VelocityEngineModel;
import org.limy.lrd.common.bean.DeployInfo;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;

/**
 * フレーム形式でHTMLを出力するWriterクラスです。
 * @author Naoki Iwami
 */
public class LrdFramedHtmlWriter implements LrdWriter {
    
    // ------------------------ Fields (model)

    /** Velocityエンジンモデル */
    private VelocityEngineModel engineModel;

    /** Lrdマルチモデル */
    private LrdMultiModel model;
    
    // ------------------------ Bind Setter Methods (framework)

    /**
     * Velocityエンジンモデルを設定します。
     * @param engineModel Velocityエンジンモデル
     */
    public void setVelocityEngineModel(VelocityEngineModel engineModel) {
        this.engineModel = engineModel;
    }

    /**
     * Lrdマルチモデルを設定します。
     * @param model Lrdマルチモデル
     */
    public void setModel(LrdMultiModel model) {
        this.model = model;
    }
    
    // ------------------------ Public Methods

    /**
     * Lrdの内容をHTML形式で出力します。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @param extInfos 追加参照情報
     * @param menuRoot メニューツリー情報
     * @param contextOrg コンテキスト情報
     * @throws LrdException 共通例外
     * @throws IOException 
     */
    public void writeHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Writer out,
            Collection<LrdTargetInfo> extInfos, LrdNode menuRoot,
            Context contextOrg) throws LrdException, IOException {
        
        Context context = createContext(repositoryBean, bean, extInfos, menuRoot, contextOrg);
        VelocityEngine engine = engineModel.getEngine(repositoryBean);
        LrdWriterUtils.writeVmTemplate(engine, "framed_html.vm", context, out);
        
    }

    /**
     * 任意のテンプレートを使ってLrdの内容をHTML形式で出力します。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @param menuRoot メニューツリー情報
     * @param tempalte テンプレート
     * @throws LrdException 共通例外
     * @throws IOException 
     */
    public void writeHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Writer out,
            LrdNode menuRoot, Template tempalte) throws LrdException, IOException {
        
        Context context = createContext(repositoryBean, bean, null, menuRoot, null);
        VelocityEngine engine = engineModel.getEngine(repositoryBean);
        LrdWriterUtils.writeVmTemplate(engine, tempalte, context, out);
        
    }

    /**
     * Lrdの内容をシンプルなHTML形式で出力します（プレビュー用）。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param out 出力先
     * @param extInfos 追加参照情報
     * @param menuRoot メニューツリー情報
     * @throws LrdException 共通例外
     * @throws IOException 
     */
    public void writeSimpleHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Writer out,
            Collection<LrdTargetInfo> extInfos, LrdNode menuRoot)
            throws LrdException, IOException {
        
        String repositoryId = repositoryBean.getId();
        Context context = new VelocityContext();
        VelocityEngine engine = engineModel.getEngine(repositoryBean);
        context.put("mainContents", createHtmlString(bean, extInfos));
        context.put("menuNodes", menuRoot);
        context.put("targetPath", bean.getPath());
        context.put("Util", new LrdVelocityMacro(model, repositoryId));
        context.put("repositoryId", model.getRepositoryBean(repositoryId).getId());
        LrdWriterUtils.writeVmTemplate(engine, "preview_html.vm", context, out);
    }

    /**
     * 全メニューをHTMLに出力します。
     * @param repositoryBean リポジトリBean
     * @param out 出力先
     * @param menuRoot メニューツリー情報
     * @throws LrdException 
     */
    public void writeAllMenu(
            RepositoryBean repositoryBean,
            Writer out, LrdNode menuRoot) throws LrdException {
        
        String repositoryId = repositoryBean.getId();
        Context context = new VelocityContext();
        
        context.put("menuNodes", menuRoot);
        context.put("Util", new LrdVelocityMacro(model, repositoryId));
        
        String url = repositoryBean.getMainDeployInfo().getDeployUrl();
        context.put("baseHref", url);
        
        VelocityEngine engine = engineModel.getEngine(repositoryBean);
        LrdWriterUtils.writeVmTemplate(engine, "allmenu.vm", context, out);
            
    }

    public void writeRecentPage(RepositoryBean repositoryBean,
            LrdDeployInfo info,
            Writer out,
            Collection<RecentEntry> recentUpdates) throws IOException,
            LrdException {
        
        Context context = new VelocityContext();
        context.put("resourceUrlBase", info.getDeployUrl()); // リソース参照ROOT

        context.put("recentUpdates", recentUpdates);
        context.put("Util", new LrdVelocityMacro(model, repositoryBean.getId()));

        VelocityEngine engine = engineModel.getEngine(repositoryBean);
        LrdWriterUtils.writeVmTemplate(engine, "rightbar.vm", context, out);
        
    }
    /**
     * 参照先情報を付加してHTML形式の出力を返します。
     * @param bean LrdBean
     * @param extInfos 参照先情報
     * @return HTML形式の出力
     * @throws IOException I/O例外
     */
    public String createHtmlString(LrdBean bean, Collection<LrdTargetInfo> extInfos)
            throws IOException {
        
        if (extInfos == null) {
            bean.setAllTargetInfos(bean.createTargetInfos());
        } else {
            bean.setAllTargetInfos(extInfos);
        }
        StringBuilder results = new StringBuilder();
        LrdWriterUtils.writeLrdHtml(bean, results);
        return results.toString();
    }

    /**
     * 対象Bean用のメニューHTMLを生成して返します。
     * @param repositoryBean リポジトリBean
     * @param bean 対象Bean
     * @param menuNode メニューノード
     * @return メニューHTML
     * @throws LrdException 
     */
    public String getMenuContents(
            RepositoryBean repositoryBean,
            LrdBean bean, LrdNode menuNode) throws LrdException {
        
        String repositoryId = repositoryBean.getId();
        Context context = new VelocityContext();
        StringWriter out = new StringWriter();
        
        context.put("Util", new LrdVelocityMacro(model, repositoryId));
        context.put("targetPath", bean.getPath());
        context.put("menuNodes", menuNode);
        context.put("baseHref", "");
        
        VelocityEngine engine = engineModel.getEngine(repositoryBean);
        LrdWriterUtils.writeVmTemplate(engine, "menu_only.vm", context, out);
            
        return out.toString();
    }
    
    // ------------------------ Private Methods

    /**
     * Context を作成します。
     * @param repositoryBean リポジトリBean
     * @param bean Lrdインスタンス
     * @param extInfos 追加参照情報
     * @param menuRoot メニューツリー情報
     * @param contextOrg コンテキスト情報
     * @return
     * @throws LrdException 
     * @throws IOException I/O例外
     */
    private Context createContext(RepositoryBean repositoryBean, LrdBean bean,
            Collection<LrdTargetInfo> extInfos, LrdNode menuRoot,
            Context contextOrg) throws LrdException, IOException {
        
        Context context = new VelocityContext(contextOrg);
        RepositoryInfo repositoryInfo = bean.getProject().getRepositoryInfo();
        String repositoryId = repositoryBean.getId();
    
        if (bean.isShowUpdateTime() && repositoryInfo != null) {
            
            String authUser = repositoryInfo.getAuthUser();
            String authPass = repositoryInfo.getAuthPass();
    
            LrdRepository repository = model.getRepository(repositoryId);
            Date timestamp = repository.getTimestamp(
                    bean.getPath().getRelativePath(), authUser, authPass);
            context.put("lastUpdate",
                    Formatters.dateFormat(Formatters.D_VIEW_8, timestamp));
        }
    
        context.put("mainContents", createHtmlString(bean, extInfos));
        context.put("menuNodes", menuRoot);
        context.put("Util", new LrdVelocityMacro(model, repositoryId));
        context.put("title", bean.getTitle());
        context.put("targetPath", bean.getPath());
        context.put("startYear", Integer.valueOf(repositoryBean.getStartYear()));
        context.put("nowYear", new SimpleDateFormat("yyyy").format(new Date()));
        context.put("author", repositoryBean.getAuthor());
        context.put("charset", repositoryBean.getViewCharset());
        context.put("repositoryName", repositoryId);
        context.put("lrdPath", bean.getPath().getRelativePath());
        return context;
    }

    
}
