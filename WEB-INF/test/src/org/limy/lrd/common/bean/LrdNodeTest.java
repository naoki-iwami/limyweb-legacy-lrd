package org.limy.lrd.common.bean;

import org.junit.Assert;
import org.junit.Test;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;

public class LrdNodeTest {

    @Test
    public void testSearchSubNode() {
        LrdNode rootNode = new LrdNode(new LrdPath(null, "root/fol1"), true);
        LrdNode childNode1 = new LrdNode(new LrdPath(null, "root/fol1/file1"), false);
        LrdNode childNode2 = new LrdNode(new LrdPath(null, "root/fol1/file2"), false);
        rootNode.addSubNode(childNode1);
        rootNode.addSubNode(childNode2);
        
        Assert.assertEquals(childNode1, rootNode.searchSubNode("file1"));
        Assert.assertEquals(childNode2, rootNode.searchSubNode("file2"));
    }

    @Test
    public void testSearchSubNodeDepth() {
        LrdNode rootNode = new LrdNode(new LrdPath(null, "root/fol1"), true);
        LrdNode folder1 = new LrdNode(new LrdPath(null, "root/fol1/fol2"), true);
        LrdNode file1 = new LrdNode(new LrdPath(null, "root/fol1/fol2/file1"), false);
        rootNode.addSubNode(folder1);
        folder1.addSubNode(file1);
        
        Assert.assertEquals(file1, rootNode.searchSubNodeDepth("fol2/file1"));
        Assert.assertEquals(folder1, rootNode.searchSubNodeDepth("fol2"));
        Assert.assertEquals(rootNode, rootNode.searchSubNodeDepth(""));
    }

    @Test
    public void testSetAllEnable() {
        LrdNode rootNode = new LrdNode(new LrdPath(null, "root/fol1"), true);
        LrdNode folder1 = new LrdNode(new LrdPath(null, "root/fol1/fol2"), true);
        LrdNode file1 = new LrdNode(new LrdPath(null, "root/fol1/fol2/file1"), false);
        rootNode.addSubNode(folder1);
        folder1.addSubNode(file1);
        
        Assert.assertTrue(rootNode.isEnable());
        Assert.assertTrue(folder1.isEnable());
        Assert.assertTrue(file1.isEnable());
        rootNode.setAllEnable(false);
        Assert.assertFalse(rootNode.isEnable());
        Assert.assertFalse(folder1.isEnable());
        Assert.assertFalse(file1.isEnable());
    }

}
