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
package org.limy.lrd.common.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Lrdファイルノードを表すクラスです。
 * @author Naoki Iwami
 */
public class LrdNode {
    
    // ------------------------ Fields
    
    /**
     * 相対パス
     */
    private LrdPath path;
    
    /**
     * フォルダかどうか
     */
    private boolean folder;
    
    /**
     * 子要素
     */
    private List<LrdNode> subNodes;

    /** 有効フラグ */
    private boolean enable = true;
    
    // ------------------------ Constructors

    /**
     * LrdNode インスタンスを構築します。
     * @param path 相対パス
     * @param folder フォルダかどうか
     */
    public LrdNode(LrdPath path, boolean folder) {
        super();
        this.path = path;
        this.folder = folder;
        if (folder) {
            this.subNodes = new ArrayList<LrdNode>();
        }
    }

    // ------------------------ Override Methods
    
    @Override
    public String toString() {
        if (subNodes == null) {
            return path.getRelativePath();
        }
        return path.getRelativePath() + "(" + subNodes.size() + ")";
    }
    
    // ------------------------ Public Methods

    /**
     * 直下の子ノードを探します。
     * @param name ノード名
     * @return 子ノード
     */
    public LrdNode searchSubNode(String name) {
        for (LrdNode node : subNodes) {
            if (name.equals(node.getPath().getName())) {
                return node;
            }
        }
        return null;
    }

    /**
     * 階層的に子ノードを探します。
     * @param name ノード名
     * @return 子ノード
     */
    public LrdNode searchSubNodeDepth(String name) {
        if (name.length() == 0) {
            return this;
        }
        if (subNodes == null) {
            return null;
        }
        
        LrdNode result = null;
        for (LrdNode node : subNodes) {
            String nodeName = node.getPath().getName();
            if (name.equals(nodeName)) {
                result = node;
                break;
            }
            if (name.startsWith(nodeName + "/")) {
                if (name.indexOf('/') < 0) {
                    result = node;
                    break;
                }
                result = node.searchSubNodeDepth(name.substring(name.indexOf('/') + 1));
                break;
            }
        }
        return result;
    }

    /**
     * 子ノードを追加します。
     * @param node 
     */
    public void addSubNode(LrdNode node) {
        subNodes.add(node);
    }
    
    /**
     * 子ノード全ての有効フラグをセットします。
     * @param b 有効フラグ
     */
    public void setAllEnable(boolean b) {
        this.enable = b;
        if (subNodes != null) {
            for (LrdNode child : subNodes) {
                child.setAllEnable(b);
            }
        }
    }

    public LrdNode deepCopy() {
        return createDeepCopy(this);
    }
    
    // ------------------------ Getter/Setter Methods

    /**
     * 親ノードを取得します。
     * @return 親ノード
     */
    public LrdPath getPath() {
        return path;
    }

    /**
     * 親ノードを設定します。
     * @param path 親ノード
     */
    public void setPath(LrdPath path) {
        this.path = path;
    }

    /**
     * フォルダかどうかを取得します。
     * @return フォルダかどうか
     */
    public boolean isFolder() {
        return folder;
    }

    /**
     * フォルダかどうかを設定します。
     * @param folder フォルダかどうか
     */
    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    /**
     * 子要素を取得します。
     * @return 子要素
     */
    public List<LrdNode> getSubNodes() {
        List<LrdNode> results = new ArrayList<LrdNode>();
        if (subNodes == null) {
            return new ArrayList<LrdNode>();
        }
        for (LrdNode node : subNodes) {
            if (node.isEnable()) {
                results.add(node);
            }
        }
        return results;
    }

    public List<LrdNode> getRawSubNodes() {
        return subNodes;
    }

    /**
     * 子要素を設定します。
     * @param subNodes 子要素
     */
    public void setSubNodes(List<LrdNode> subNodes) {
        this.subNodes = subNodes;
    }


    /**
     * 有効フラグを取得します。
     * @return 有効フラグ
     */
    public boolean isEnable() {
        return enable;
    }
    
    /**
     * 有効フラグを設定します。
     * @param enable 有効フラグ
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    // ------------------------ Private Methods

    private static LrdNode createDeepCopy(LrdNode root) {
        LrdNode result = new LrdNode(root.path, root.folder);
        if (root.subNodes != null) {
            for (LrdNode child : root.subNodes) {
                result.addSubNode(createDeepCopy(child));
            }
        }
        return result;
    }

}
