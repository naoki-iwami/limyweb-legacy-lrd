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
package org.limy.lrd;

import java.io.IOException;
import java.util.Collection;

import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.RepositoryBean;

/**
 * Lrd参照情報を作成するインターフェイスです。
 * @author Naoki Iwami
 */
public interface TargetInfoCreator {

    /**
     * 参照情報を作成して返します。
     * @param repositoryBean リポジトリBran
     * @param bean LrdBean
     * @return 参照情報
     * @throws LrdException 共通例外
     * @throws IOException I/O例外
     */
    Collection<LrdTargetInfo> createTargetInfos(
            RepositoryBean repositoryBean,
            LrdBean bean)
            throws LrdException, IOException;

}
