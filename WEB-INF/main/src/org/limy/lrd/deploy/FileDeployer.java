/*
 * Created 2007/08/07
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.util.LrdUtils;

/**
 * File形式のデプロイを担当します。
 * @author Naoki Iwami
 */
public class FileDeployer extends BaseDeployer {
    
    /**
     * リポジトリBean
     */
    private final RepositoryBean repositoryBean;

    // ------------------------ Constructors

    /**
     * FileDeployer インスタンスを構築します。
     * @param repositoryBean リポジトリBean
     * @param session Webセッション
     */
    public FileDeployer(
            RepositoryBean repositoryBean, LimyWebSession session) {
        
        super(session);
        this.repositoryBean = repositoryBean;
    }
    
    // ------------------------ Implement Methods

    public void deployLrd(String lrdPath, String outHtml) throws IOException, LrdException {
        
        File baseDir = repositoryBean.getLocalContentFile();
        String uploadPath = LrdUtils.getHtmlPath(lrdPath);
        File targetFile = new File(baseDir, uploadPath);
        
        targetFile.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(targetFile);
        try {
            IOUtils.write(outHtml, out, repositoryBean.getViewCharset());
        } finally {
            out.close();
        }
        
    }

    public void deployFile(String path, byte[] contents) throws IOException,
            LrdException {
        
        File baseDir = repositoryBean.getLocalContentFile();
        File targetFile = new File(baseDir, path);

        targetFile.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(targetFile);
        try {
            IOUtils.write(contents, out);
        } finally {
            out.close();
        }
        
    }
    
    public void deployStaticFiles(File srcDir, String destDirName) throws IOException {
        
        File baseDir = repositoryBean.getLocalContentFile();
        File destDir = new File(baseDir, destDirName);
        if (!destDir.exists()) {
            // 転送先ディレクトリが存在しない場合だけ処理
            destDir.mkdirs();
            for (String fileName : srcDir.list(FileFileFilter.FILE)) {
                FileUtils.copyFile(new File(srcDir, fileName), new File(destDir, fileName));
            }
        }
    }
    
    public void deleteDir(String dirName) throws IOException {
        File baseDir = repositoryBean.getLocalContentFile();
        File destDir = new File(baseDir, dirName);
        FileUtils.deleteDirectory(destDir);
    }

    // ------------------------ Getter/Setter Methods

    /**
     * リポジトリBeanを取得します。
     * @return リポジトリBean
     */
    public RepositoryBean getRepositoryBean() {
        return repositoryBean;
    }



}
