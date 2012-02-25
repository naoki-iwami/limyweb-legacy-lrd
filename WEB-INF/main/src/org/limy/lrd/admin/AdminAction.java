/*
 * Created 2007/07/29
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
package org.limy.lrd.admin;

import java.io.File;

import org.limy.common.web.WebworkUtils;

/**
 * @author Naoki Iwami
 */
public class AdminAction extends AbstractAdminAction {

    // ------------------------ Override Methods

    @Override
    protected String doExecuteAdmin() {
//        checkCustomizeLocalContext();
        return SUCCESS;
    }

    // ------------------------ Web Getter Methods
    
    public String getContextXmlPath() {
        return new File(System.getenv("CATALINA_HOME"),
                "conf/Catalina/localhost/limyweb-lrd.xml").getAbsolutePath()
                .replaceAll("\\\\", "/");
    }
    
    // ------------------------ Private Methods

//    /**
//     * ローカルコンテキスト場所をカスタマイズしたリポジトリに対して、context/XXX にハードリンクを張ります。
//     */
//    private void checkCustomizeLocalContext() {
//        for (RepositoryBean bean : getconfigRootBean().getRepositoryBeans()) {
//            File baseDir = bean.getLocalContentFile();
//            if (LrdUtils.getDefaultLocalContentFile(bean.getId()).equals(baseDir)) {
//                // デフォルト以外のコンテキスト場所を指定したリポジトリのみ処理
//            }
//        }
//        
//    }


}
