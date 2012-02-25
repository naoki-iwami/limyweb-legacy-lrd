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
package org.limy.lrd.deploy;

import org.limy.common.web.LimyWebSession;

/**
 * 基底デプロイ担当クラスです。
 * @author Naoki Iwami
 */
public abstract class BaseDeployer implements Deployable {

    // ------------------------ Fields

    /** Webセッション */
    private final LimyWebSession session;
    
    /** 現在デプロイした数 */
    private int deployCount;
    
    /** 今回の全デプロイ数 */
    private int allDeployCount;
    
    // ------------------------ Constructors

    public BaseDeployer(LimyWebSession session) {
        super();
        this.session = session;
    }

    // ------------------------ Implement Methods

    public void addDeployCount(int number) {
        deployCount += number;
    }

    public int getAllDeployCount() {
        return allDeployCount;
    }

    public int getDeployCount() {
        return deployCount;
    }

    public LimyWebSession getSession() {
        return session;
    }

    public void setAllDeployCount(int count) {
        allDeployCount = count;
    }

}
