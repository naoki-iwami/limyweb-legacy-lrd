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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.velocity.context.Context;
import org.limy.common.util.FtpUtils;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.DeployInfo;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;

/**
 * LrdファイルのFTP形式デプロイを担当します。
 * @author Naoki Iwami
 */
public class LrdFtpDeployerImpl extends BaseLrdDeployer implements LrdDeployer {

    /**
     * @author Naoki Iwami
     */
    private interface LrdDeployer {
        void deploy(RepositoryBean repositoryBean,
                Context context, Deployable deployable, Object deployInfo)
                throws LrdException, IOException;
    }

    // ------------------------ Constants

    /** logger */
    private static final Log LOG = LogFactory.getLog(LrdFtpDeployerImpl.class);
    
    // ------------------------ Implement Methods

    public void deploySingleLrd(RepositoryBean repositoryBean, String lrdPath,
            DeployInfo deployInfo)
            throws IOException, LrdException {
        
        LrdDeployInfo ftpDeployInfo = (LrdDeployInfo)deployInfo;
        deployWithFtp(repositoryBean, null, ftpDeployInfo, new LrdDeployer() {

            public void deploy(RepositoryBean repositoryBean,
                    Context context, Deployable deployable, Object deployInfo)
                    throws LrdException, IOException {
                deployLrdRecursive(repositoryBean, (String)deployInfo, context, deployable);

            }
            
        }, lrdPath);
    }

    public void deployAllLrd(RepositoryBean repositoryBean, LimyWebSession session,
            DeployInfo deployInfo)
            throws IOException, LrdException {
        
        LrdDeployInfo ftpDeployInfo = (LrdDeployInfo)deployInfo;
        
        LrdNode root = repositoryBean.getRepository().getDirectoryRoot();

        deployWithFtp(repositoryBean, session, ftpDeployInfo, new LrdDeployer() {

            public void deploy(RepositoryBean repositoryBean,
                    Context context, Deployable deployable, Object deployInfo)
                    throws LrdException, IOException {
                
                deployLrdAllFiles(repositoryBean, context, deployable, (LrdNode)deployInfo);
            }
            
        }, root);
    }

    // ------------------------ Private Methods

    /**
     * FTP経由でデプロイします。
     * @param repositoryBean リポジトリBean
     * @param session Webセッション
     * @param ftpDeployInfo FTPデプロイ先
     * @param deployer デプロイ担当
     * @param deployInfo デプロイ情報詳細
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    private void deployWithFtp(RepositoryBean repositoryBean,
            LimyWebSession session,
            LrdDeployInfo ftpDeployInfo,
            LrdDeployer deployer, Object deployInfo) throws IOException, LrdException {

        FTPClient client = new FTPClient();
        try {

            // FTPに接続
            FtpUtils.connectFtp(client, ftpDeployInfo.getFtpInfo());

            Context context = createContext(repositoryBean, ftpDeployInfo.getDeployUrl());
            
            Deployable deployable = new FtpDeployer(repositoryBean, client,
                    ftpDeployInfo.getFtpInfo(), session);
            deployable.setAllDeployCount(countAllDeployFiles(repositoryBean));
            deployStaticFiles(repositoryBean, deployable, true);
            
            deployer.deploy(repositoryBean, context, deployable, deployInfo);
        } finally {
            try {
                FtpUtils.disconnect(client);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

    }

    public void deployStaticFile(RepositoryBean repositoryBean,
            String relativePath, byte[] contents) throws IOException,
            LrdException {
        // TODO Auto-generated method stub
        
    }
    
}
