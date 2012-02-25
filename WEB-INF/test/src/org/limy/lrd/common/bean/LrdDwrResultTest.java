package org.limy.lrd.common.bean;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class LrdDwrResultTest {

    @Test
    public void testCreateAdminLinks() throws Exception {
        LrdDwrResult obj = new LrdDwrResult();
        
        obj.setMainString(new String(IOUtils.toByteArray(
                LrdDwrResultTest.class.getResourceAsStream("sample.html")),
                "UTF-8"));
        
        Assert.assertTrue(obj.getMainString().indexOf("javascript:changeLocation") < 0);
        obj.createAdminLinks("program/index.lrd");
        Assert.assertTrue(obj.getMainString().indexOf("javascript:changeLocation") >= 0);
    }

}
