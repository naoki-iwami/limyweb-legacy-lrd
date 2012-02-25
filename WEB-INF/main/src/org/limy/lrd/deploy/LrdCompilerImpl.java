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
package org.limy.lrd.deploy;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.velocity.context.Context;
import org.limy.lrd.LrdDao;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdNode;

/**
 * Lrdをコンパイルする実装クラスです。
 * @author Naoki Iwami
 */
public class LrdCompilerImpl implements LrdCompiler {

    // ------------------------ Fields

    /** Lrd出力 */
    private LrdWriter lrdWriter;
    
    /** DAO */
    private LrdDao dao;

    /** キャッシュマネージャ */
    private LrdCacheManager cacheManager;

    // ------------------------ Getter/Setter Methods

    /**
     * Lrd出力を設定します。
     * @param lrdWriter Lrd出力
     */
    public void setLrdWriter(LrdWriter lrdWriter) {
        this.lrdWriter = lrdWriter;
    }

    /**
     * DAOを設定します。
     * @param dao DAO
     */
    public void setDao(LrdDao dao) {
        this.dao = dao;
    }

    /**
     * キャッシュマネージャを設定します。
     * @param cacheManager キャッシュマネージャ
     */
    public void setCacheManager(LrdCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    // ------------------------ Implement Methods

    /**
     * LrdBeanをコンパイルしてHTML形式で返します。
     * @param repositoryBean リポジトリBean
     * @param bean LrdBean
     * @param extInfos リンク先情報
     * @param context コンテキスト情報
     * @return HTML形式文字列
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public String compileToHtml(
            RepositoryBean repositoryBean,
            LrdBean bean, Collection<LrdTargetInfo> extInfos,
            Context context)
            throws IOException, LrdException {
        
        context.put("comments", dao.getLrdComments(repositoryBean.getId(),
                bean.getPath().getRelativePath()));
        
        LrdNode menuRoot = cacheManager.getMenuTree(repositoryBean.getId(), bean);

        // HTML形式で出力して返却
        CharArrayWriter writer = new CharArrayWriter();
        lrdWriter.writeHtml(repositoryBean, bean, writer, extInfos, menuRoot, context);
        return writer.toString();
        
    }

}
