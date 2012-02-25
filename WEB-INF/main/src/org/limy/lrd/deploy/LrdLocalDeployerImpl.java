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

import java.io.File;
import java.io.IOException;

import org.apache.velocity.context.Context;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.DeployInfo;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.util.LrdUtils;

/**
 * LrdファイルのLOCAL形式デプロイを担当します。
 * @author Naoki Iwami
 */
public class LrdLocalDeployerImpl extends BaseLrdDeployer implements LrdDeployer {

    // ------------------------ Implement Methods

    /**
     * 単一のLrdファイルをデプロイします。
     * @param repositoryBean リポジトリBean
     * @param lrdPath Lrdパス
     * @param deployInfo デプロイ先情報
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public void deploySingleLrd(RepositoryBean repositoryBean, String lrdPath,
            DeployInfo deployInfo)
            throws IOException, LrdException {
        
        Deployable deployable = new FileDeployer(repositoryBean, null);
        deployStaticFiles(repositoryBean, deployable, false);
        
        Context context = createContext(repositoryBean,
                ((LrdDeployInfo)deployInfo).getDeployUrl());
//                LrdUtils.getLocalContentUrl(repositoryBean.getId()));
//        // TODO リリース時にはコメントアウトすること
//        Context context = createContext(repositoryBean,
//                "http://www.limy.org/program");
        
        // デプロイ（依存ファイルも再帰的にデプロイ）
        deployLrdRecursive(repositoryBean, lrdPath, context, deployable);
        
    }

    /**
     * 全Lrdファイルをデプロイします。
     * @param repositoryBean リポジトリBean
     * @param session Webセッション
     * @param deployInfo デプロイ先情報
     * @throws IOException I/O例外
     * @throws LrdException 共通例外
     */
    public void deployAllLrd(RepositoryBean repositoryBean, LimyWebSession session,
            DeployInfo deployInfo)
            throws IOException, LrdException {
        
        Deployable deployable = new FileDeployer(repositoryBean, session);
        deployable.setAllDeployCount(countAllDeployFiles(repositoryBean));
        deployStaticFiles(repositoryBean, deployable, true);
        
        Context context = createContext(repositoryBean,
                ((LrdDeployInfo)deployInfo).getDeployUrl());
//                LrdUtils.getLocalContentUrl(repositoryBean.getId()));
//        // TODO リリース時にはコメントアウトすること
//        // http://localhost:8080/limyweb-lrd/context/PROGRAM
//        Context context = createContext(repositoryBean,
//                "http://www.limy.org/program");
        
        
        // デプロイ（依存ファイルも再帰的にデプロイ）
        LrdNode root = repositoryBean.getRepository().getDirectoryRoot();
        deployLrdAllFiles(repositoryBean, context, deployable, root);
    }


    public void deployStaticFile(RepositoryBean repositoryBean,
            String relativePath, byte[] contents) throws IOException, LrdException {
        
        Deployable deployable = new FileDeployer(repositoryBean, null);
        deployable.deployFile(relativePath, contents);
        
    }
    
}
