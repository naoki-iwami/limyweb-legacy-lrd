/*
 * Created 2006/09/01
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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import org.limy.common.svn.CommitFileInfo;
import org.limy.common.util.Formatters;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.bean.LrdCommentInfo;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.util.LrdUtils;

/**
 * Lrdで使用するVelocityマクロクラスです。
 * @author Naoki Iwami
 */
public class LrdVelocityMacro {
    
    /** Lrdキャッシュマネージャ */
    private LrdCacheManager manager = LrdCacheManagerImpl.getInstance();
    
    /** Lrdリポジトリ */
    private final LrdRepository repository;

    /** リポジトリID */
    private String repositoryId;
    
    public LrdVelocityMacro(LrdMultiModel model, String repositoryId) {
        this.repository = model.getRepository(repositoryId);
        this.repositoryId = repositoryId;
    }
    
    public boolean isDirectory(Object value) {
        if (value instanceof LrdNode) {
            return ((LrdNode)value).isFolder();
        }
        return false;
    }
    
    public String concat(String s1, String s2, String s3) {
        return s1 + s2 + s3;
    }
    
    public String createId(Object value) {
        if (value instanceof LrdNode) {
            LrdNode node = (LrdNode)value;
            return node.getPath().getRelativePath();
        }
        return null;
    }
    
    public String getLrdTitle(LrdPath path) throws LrdException, IOException {
        CommitFileInfo lrdInfo = repository.getRepositoryFile(
                path.getRelativePath());
        if (lrdInfo != null) {
            LrdBean bean = manager.getBean(repositoryId, lrdInfo);
            if (bean.getTitle() == null) {
                String s = path.getRelativePath();
                return s.substring(s.lastIndexOf('/') + 1, s.lastIndexOf('.'));
            }
            return bean.getTitle();
        }
        return "Untitle";
    }

    public String getIndexLrdTitle(LrdPath path) throws IOException {
        return manager.getIndexLrdTitle(repositoryId, path);
    }

    public boolean isIndex(LrdPath path) throws IOException {
        return "index.lrd".equals(path.getName());
    }

    public boolean isLrd(LrdPath path) throws IOException {
        return path.getName().endsWith(".lrd");
    }

    public String getHtmlPath(LrdPath path) {
        return LrdUtils.getHtmlPath(path.getRelativePath());
    }
    
    public String markStrongStart(LrdPath path1, LrdPath path2) {
        return equalsPath(path1, path2) ? "<strong>" : "";
    }

    public String markStrongEnd(LrdPath path1, LrdPath path2) {
        return equalsPath(path1, path2) ? "</strong>" : "";
    }

    /**
     * 日付をW3C形式で取得します。
     * @param time 日付
     * @return W3C形式の日付
     */
    public String w3cDate(Timestamp time) {
        return Formatters.dateFormat(Formatters.W3C, time);
    }

    public int getSize(Object obj) {
        if (obj instanceof Collection) {
            Collection<? extends Object> col = (Collection<? extends Object>)obj;
            return col.size();
        }
        return 0;
    }
    
    public String getLatestDate(Object obj) {
        if (obj instanceof Collection) {
            Collection<? extends Object> col = (Collection<? extends Object>)obj;
            Object lastObj = null;
            for (Iterator it  = col.iterator(); it.hasNext();) {
                lastObj = it.next();
            }
            if (lastObj instanceof LrdCommentInfo) {
                LrdCommentInfo info = (LrdCommentInfo)lastObj;
                return " " + info.getTime();
            }
        }
        return null;
    }
    
    // ------------------------ Private Methods

    /**
     * 二つのパスが同じかどうか比較します。
     * @param path1 パス1
     * @param path2 パス2
     * @return 同じだったらtrue
     */
    private boolean equalsPath(LrdPath path1, LrdPath path2) {
        if (path1.getRelativePath().equals(path2.getRelativePath())) {
            return true;
        }
        return path1.createNewPath("index.lrd").getRelativePath().equals(path2.getRelativePath());
    }
    
}
