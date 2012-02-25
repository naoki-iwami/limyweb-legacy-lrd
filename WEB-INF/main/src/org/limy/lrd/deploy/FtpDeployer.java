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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.limy.common.ftp.FtpInfo;
import org.limy.common.util.FtpUtils;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.util.LrdUtils;

/**
 * @author Naoki Iwami
 */
public class FtpDeployer extends BaseDeployer {

    /** logger */
    private static final Log LOG = LogFactory.getLog(FtpDeployer.class);
    
    // ------------------------ Fields

    /** リポジトリBean */
    private final RepositoryBean repositoryBean;
    
    /** FTP接続 */
    private final FTPClient client;
    
    /** FTP情報 */
    private final FtpInfo ftpInfo;

    // ------------------------ Constructors

    /**
     * FtpDeployer インスタンスを構築します。
     * @param repositoryBean リポジトリBean
     * @param client FTP接続
     * @param ftpInfo FTP情報
     * @param session Webセッション
     */
    public FtpDeployer(RepositoryBean repositoryBean, FTPClient client, FtpInfo ftpInfo,
            LimyWebSession session) {
        super(session);
        this.repositoryBean = repositoryBean;
        this.client = client;
        this.ftpInfo = ftpInfo;
    }

    // ------------------------ Implement Methods

    public void deployLrd(String lrdPath, String outHtml) throws IOException, LrdException {
        
        String uploadPath = LrdUtils.getHtmlPath(lrdPath);
        deployByteContents(uploadPath, getBytesEUC(outHtml, repositoryBean.getViewCharset()));
    }
    
    public void deployFile(String path, byte[] contents)
            throws IOException, LrdException {
        
        deployByteContents(path, contents);

    }

    public void deployStaticFiles(File srcDir, String destDirName)
            throws IOException {
        
        for (String fileName : srcDir.list(FileFileFilter.FILE)) {
            FileInputStream in = new FileInputStream(new File(srcDir, fileName));
            FtpUtils.uploadFileFtp(client, ftpInfo.getPath(),
                    destDirName + "/" + fileName, in);
            in.close();
        }
    }

    public void deleteDir(String dirName) throws IOException {
        // not support
        LOG.debug("ディレクトリ削除には対応していません。");
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * FTP接続を取得します。
     * @return FTP接続
     */
    public FTPClient getClient() {
        return client;
    }

    /**
     * FTP情報を取得します。
     * @return FTP情報
     */
    public FtpInfo getFtpInfo() {
        return ftpInfo;
    }

    // ------------------------ Private Methods

    /**
     * ファイルをFTP経由でデプロイします。
     * @param path リポジトリルートからの相対パス
     * @param contents ファイル内容
     * @throws IOException I/O例外
     * @throws LrdException 
     */
    private void deployByteContents(String path, byte[] contents)
            throws IOException, LrdException {

        ByteArrayInputStream inputStream = new ByteArrayInputStream(contents);
        boolean success;
        try {
            success = FtpUtils.uploadFileFtp(client,
                    ftpInfo.getPath(),
                    path,
                    inputStream);
        } finally {
            inputStream.close();
        }
        
        if (!success) {
            // アップロードに失敗
            LOG.debug("Fail to upload : " + path);
            throw new LrdException(path + " のデプロイに失敗しました。");
        }
    }

    /**
     * 3バイトEUC文字に対応したbyte配列への変換を行います。
     * @param lines 文字列
     * @param charset 文字セット
     * @return byte配列
     * @throws UnsupportedEncodingException 文字セット未サポート例外
     */
    private byte[] getBytesEUC(String lines, String charset)
            throws UnsupportedEncodingException {
        
        if ("EUC-JP".equals(charset)) {
            // EUC-JPでは、Type Aの「～」をType Bの「～」に変換する
            return lines.replace('\uff5e', '\u301c').getBytes(charset);
        }
        return lines.getBytes(charset);
    }

}
