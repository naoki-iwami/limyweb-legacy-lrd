/*
 * Created 2006/04/28
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
package org.limy.lrd.repository.svn;

import java.util.StringTokenizer;

import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.tmatesoft.svn.core.ISVNDirEntryHandler;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;

/**
 * SVNディレクトリエントリ結果を受け取るハンドラクラスです。
 * @author Naoki Iwami
 */
/* package */ class LrdSVNDirEntryHandler implements ISVNDirEntryHandler {
    
    /**
     * Lrdプロジェクト
     */
    private LrdProject project;
    
    /**
     * ルートノード
     */
    private LrdNode root;
    
    /**
     * LrdSVNDirEntryHandlerインスタンスを構築します。
     * @param project Lrdプロジェクト
     */
    public LrdSVNDirEntryHandler(LrdProject project) {
        this.project = project;
        root = new LrdNode(new LrdPath(project, ""), true);
    }

    /**
     * @param dirEntry 
     */
    public void handleDirEntry(SVNDirEntry dirEntry) {
        
        String path = dirEntry.getRelativePath();
        
        if (SVNNodeKind.DIR == dirEntry.getKind()) {

            int index = path.lastIndexOf('/');
            if (index <= 0) {
                // ルート直下
                LrdNode node = new LrdNode(new LrdPath(project, path), true);
                if (path.charAt(0) == '.') {
                    // ルート直下の"."で始まるフォルダは無効とする
                    node.setEnable(false);
                }
                root.addSubNode(node);
            } else {
                String baseDir = path.substring(0, index);
                StringTokenizer tokenizer = new StringTokenizer(baseDir, "/");
                LrdNode now = root;
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    now = now.searchSubNode(token);
                }
                LrdNode node = new LrdNode(new LrdPath(project, path), true);
                now.addSubNode(node);
            }
        }
        if (SVNNodeKind.FILE == dirEntry.getKind()) {
            int index = path.lastIndexOf('/');
            LrdNode now = root;
            if (index >= 0) {
                String baseDir = path.substring(0, index);
                StringTokenizer tokenizer = new StringTokenizer(baseDir, "/");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    now = now.searchSubNode(token);
                }
            }
            
            LrdNode node = new LrdNode(new LrdPath(project, path), false);
            now.addSubNode(node);
        }
    }

    /**
     * ルートノードを取得します。
     * @return ルートノード
     */
    public LrdNode getRoot() {
        return root;
    }

}
