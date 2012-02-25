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
package org.limy.lrd.repository.svn;

import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.limy.common.repository.RepositoryInfo;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * Svnkit関連のユーティリティクラスです。
 * @author Naoki Iwami
 */
public final class SvnkitUtils {
    
    static {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
//        SVNRepositoryFactoryImpl.setup();
    }

    /**
     * private constructor
     */
    private SvnkitUtils() { }
    
    /**
     * SVNリポジトリの全ツリーを取得します。
     * @param bean リポジトリBean
     * @return リポジトリルート
     * @throws LrdException Lrd共通例外
     */
    public static LrdNode getRepositoryRootNode(RepositoryBean bean) throws LrdException {
        
        RepositoryInfo repositoryInfo = bean.getRepositoryInfo();
        
//        SVNLogClient logClient = SVNClientManager.newInstance(
//                SVNWCUtil.createDefaultOptions(true),
//                repositoryInfo.getAuthUser(),
//                repositoryInfo.getAuthPass()).getLogClient();
//        SVNRepositoryFactoryImpl.setup();
        
        LrdNode root = new LrdNode(new LrdPath(bean.getProject(), ""), true);

//        boolean recursive = true;
//        
//        LrdSVNDirEntryHandler handler = new LrdSVNDirEntryHandler(bean.getProject());
        try {
            SVNRepository repository = SVNRepositoryFactory.create(
                    SVNURL.parseURIDecoded(repositoryInfo.getRepositoryUrl()));
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
                    repositoryInfo.getAuthUser(), repositoryInfo.getAuthPass());
            repository.setAuthenticationManager(authManager);
            listEntries(repository, "", root);
            repository.closeSession();
//            logClient.doList(
//                    SVNURL.parseURIEncoded(
//                            repositoryInfo.getRepositoryUrl()),
//                    SVNRevision.UNDEFINED,
//                    SVNRevision.HEAD, recursive,
//                    handler);
        } catch (SVNException e) {
            throw new LrdException(e);
        }
        return root;

    }

    public static void listEntries(SVNRepository repository, String basePath,
            LrdNode root) throws SVNException {
        
        LrdProject project = root.getPath().getProject();
        
        Collection entries = repository
                .getDir(basePath, -1, null, (Collection)null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry)iterator.next();
            String path = ((basePath.length() == 0) ? "" : basePath + "/") + entry.getRelativePath();
            
            if (entry.getKind() == SVNNodeKind.DIR) {
                int index = path.lastIndexOf('/');
                LrdNode node;
                if (index <= 0) {
                    // ルート直下
                    node = new LrdNode(new LrdPath(project, path), true);
                    if (path.charAt(0) == '.') {
                        // ルート直下の"."で始まるフォルダは無効とする
                        node.setEnable(false);
                    }
                    root.addSubNode(node);
                } else {
                    String baseDir = path.substring(0, index);
                    StringTokenizer tokenizer = new StringTokenizer(baseDir, "/");
//                    LrdNode now = root;
//                    while (tokenizer.hasMoreTokens()) {
//                        String token = tokenizer.nextToken();
//                        now = now.searchSubNode(token);
//                    }
                    node = new LrdNode(new LrdPath(project, path), true);
                    root.addSubNode(node);
                }
                listEntries(repository,
                        (basePath.equals("")) ? entry.getName()
                        : basePath + "/" + entry.getName(), node);
            } else {
//                int index = path.lastIndexOf('/');
//                LrdNode now = root;
//                if (index >= 0) {
//                    String baseDir = path.substring(0, index);
//                    StringTokenizer tokenizer = new StringTokenizer(baseDir, "/");
//                    while (tokenizer.hasMoreTokens()) {
//                        String token = tokenizer.nextToken();
//                        now = now.searchSubNode(token);
//                    }
//                }
                
                LrdNode node = new LrdNode(new LrdPath(project, path), false);
                root.addSubNode(node);
            }
        }
    }
    
}
