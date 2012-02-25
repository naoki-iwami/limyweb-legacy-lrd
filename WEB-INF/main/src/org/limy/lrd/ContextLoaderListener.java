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
package org.limy.lrd;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.opensymphony.webwork.config.ServletContextSingleton;

/**
 * アプリケーション開始／終了時に呼び出されます。
 * @author Naoki Iwami
 */
public class ContextLoaderListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        // ServletContext をstaticに格納
        ServletContextSingleton.getInstance().setServletContext(event.getServletContext());
    }

    public void contextDestroyed(ServletContextEvent event) {
        // do nothing
    }


}
