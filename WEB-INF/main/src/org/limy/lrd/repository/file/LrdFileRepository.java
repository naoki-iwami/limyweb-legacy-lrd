/*
 * Created 2007/07/08
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
package org.limy.lrd.repository.file;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.bean.LrdLightBean;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;

/**
 * ファイルによるリポジトリ実装クラスです。
 * @author Naoki Iwami
 */
public final class LrdFileRepository implements LrdRepository {

    // ------------------------ Fields

    /** リポジトリBean */
    private final RepositoryBean repositoryBean;
    
    // ------------------------ Constructors

    public LrdFileRepository(RepositoryBean repositoryBean) {
        this.repositoryBean = repositoryBean;
    }
    
    // ------------------------ Implement Methods

    public void addFile(String lrdPath) throws LrdException {
        File file = getRealFile(lrdPath);
        if (file.exists()) {
            throw new LrdException(lrdPath + " は既にリポジトリ上に存在します。");
        }
        try {
            file.getParentFile().mkdirs();
            FileUtils.touch(file);
        } catch (IOException e) {
            throw new LrdException(e);
        }
    }


    public void addDirectory(String lrdPath) throws LrdException {
        getRealFile(lrdPath).mkdirs();
    }
    
    public void commitMultiFile(CommitFileInfo[] commitInfos) throws LrdException {
        
        writeFiles(commitInfos, false);
    }

    public void addAndcommitMultiFile(CommitFileInfo[] commitInfos)
            throws LrdException {

        writeFiles(commitInfos, true);
    }

    public String getAllMenu(LrdWriter writer)
            throws LrdException {
        
        StringWriter out = new StringWriter();
        try {
            writer.writeAllMenu(repositoryBean, out, getDirectoryRoot());
        } catch (IOException e) {
            throw new LrdException(e);
        }
        return out.toString();
    }

    public LrdNode getDirectoryRoot() throws LrdException {
        LrdNode root = new LrdNode(new LrdPath(getProject(), ""), true);
        for (File file : getBaseDir().listFiles()) {
            if (file.getName().startsWith(".")) {
                continue;
            }
            createNode(root, file);
        }
        return root;
    }
    
    public LrdPath[] getFilesFromDirectory(
            String repositoryUrl) throws LrdException {
        
        File targetDir = getFile(repositoryUrl);
        if (!targetDir.isDirectory()) {
            throw new IllegalStateException(targetDir + " はディレクトリではありません。");
        }
        
        Collection<LrdPath> results = new ArrayList<LrdPath>();
        String basePath = repositoryUrl;
        if (repositoryUrl.length() != 0 && !repositoryUrl.endsWith("/")) {
            basePath += "/";
        }
        for (File file : targetDir.listFiles()) {
            if (file.isFile()) {
                results.add(new LrdPath(getProject(), basePath + file.getName()));
            }
        }
        return results.toArray(new LrdPath[results.size()]);
    }

    public CommitFileInfo getRepositoryFile(String repositoryUrl)
            throws LrdException {
        
        File file = getFile(repositoryUrl);
        try {
            byte[] contents = FileUtils.readFileToByteArray(file);
            return new CommitFileInfo(repositoryUrl, contents,
                    repositoryBean.getRepositoryCharset());
        } catch (IOException e) {
            throw new LrdException(e);
        }
    }

    public CommitFileInfo[] getRepositoryFiles(
            String[] lrdPaths) throws LrdException {
        
        Collection<CommitFileInfo> results = new ArrayList<CommitFileInfo>();
        for (String lrdPath : lrdPaths) {
            results.add(getRepositoryFile(lrdPath));
        }
        return results.toArray(new CommitFileInfo[results.size()]);
    }

    public Date getTimestamp(
            String repositoryUrl, String authUser,
            String authPass) throws LrdException {
        
        File file = getFile(repositoryUrl);
        if (!file.exists()) {
            throw new LrdException("ファイル[" + file + "] が存在しません。");
        }
        return new Date(file.lastModified());
    }

    public boolean isDirectory(String repositoryUrl)
            throws LrdException {
        
        return getFile(repositoryUrl).isDirectory();
    }

    // ------------------------ Private Methods

    private File getBaseDir() {
        return new File(repositoryBean.getRepositoryInfo().getRepositoryUrl());
    }

    private File getFile(String repositoryUrl, boolean force) {
        File file = new File(getBaseDir(), repositoryUrl);
        if (force) {
            return file;
        }
        if (file.exists()) {
            return file;
        }
        return new File(repositoryUrl); // 絶対パスの場合
    }
    
    private File getFile(String repositoryUrl) {
        return getFile(repositoryUrl, false);
    }

    /**
     * リポジトリにあるファイルの実体を返します。
     * @param relativeUrl ルートからの相対パス
     * @return
     */
    private File getRealFile(String relativeUrl) {
        return new File(getBaseDir(), relativeUrl);
    }

    /**
     * ノードに新規ノードを追加します。
     * @param root ルートノード
     * @param targetFile 対象ファイル
     */
    private void createNode(LrdNode root, File targetFile) {
        if (targetFile.isDirectory()) {
            LrdNode subNode = new LrdNode(
                    root.getPath().createNewPath(targetFile.getName()), true);
            root.addSubNode(subNode);
            for (File child : targetFile.listFiles()) {
                createNode(subNode, child);
            }
        } else {
            root.addSubNode(
                    new LrdNode(root.getPath().createNewPath(targetFile.getName()), false)
            );
        }
    }
    
    private LrdProject getProject() {
        return repositoryBean.getProject();
    }

    private void writeFiles(CommitFileInfo[] commitInfos, boolean force)
            throws LrdException {
        for (CommitFileInfo info : commitInfos) {
            File file = getFile(info.getPath(), force);
            try {
                FileUtils.writeByteArrayToFile(file, info.getContents());
            } catch (IOException e) {
                throw new LrdException(e);
            }
        }
    }

    public Collection<CommitFileInfo> getRecentUpdateFiles(int count) throws LrdException {
        // TODO Auto-generated method stub
        return null;
    }

}
