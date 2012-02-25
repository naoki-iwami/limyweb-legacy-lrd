package org.limy.lrd.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.directwebremoting.ScriptSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.util.LrdUtils;

public class FileDeployerTest {

    private RepositoryBean repositoryBean;
    private LimyWebSession session;
    private FileDeployer deployer;

    @Before
    public void setUp() throws Exception {
        
        FileUtils.deleteDirectory(LrdUtils.getDefaultLocalContentFile("__test__"));
        
        repositoryBean = new RepositoryBean();
        repositoryBean.setId("__test__");
        session = new LimyWebSession(new ArrayList<ScriptSession>());
        deployer = new FileDeployer(repositoryBean, session);

    }
    
    @Test
    public void testDeploy() throws LrdException, IOException {
        deployer.deployLrd("index.lrd", "<html></html>");
        assertExtstFile("index.html");
        
        deployer.deployLrd("fol/index.lrd", "<html></html>");
        assertExtstFile("fol/index.html");
    }

    @Test
    public void testDeployStatic() throws LrdException, IOException {
        deployer.deployStaticFiles(new File("images"), "images");
        assertExtstFile("images");
        assertExtstFile("images/circle.gif");

        // 2回目はデプロイしない（imagesが存在するので）
        deployer.deployStaticFiles(new File("images"), "images");

        deployer.deleteDir("images");
        
        assertNotExtstFile("images");
        assertNotExtstFile("images/circle.gif");

    }

    /**
     * 指定したファイルが存在することをチェックします。<br>
     * 存在しなかった場合には Assertion が発生します。
     * @param url リポジトリルートからの相対パス
     * @throws IOException 
     */
    private void assertExtstFile(String url) throws IOException {
        Assert.assertTrue(new File(LrdUtils.getDefaultLocalContentFile("__test__"), url).exists());
    }

    /**
     * 指定したファイルが存在しないことをチェックします。<br>
     * 存在した場合には Assertion が発生します。
     * @param url リポジトリルートからの相対パス
     * @throws IOException 
     */
    private void assertNotExtstFile(String url) throws IOException {
        Assert.assertFalse(new File(LrdUtils.getDefaultLocalContentFile("__test__"), url).exists());
    }

}
