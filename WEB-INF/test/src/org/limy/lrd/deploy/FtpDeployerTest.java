package org.limy.lrd.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.directwebremoting.ScriptSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.limy.common.ftp.FtpInfo;
import org.limy.common.util.FtpUtils;
import org.limy.common.web.LimyWebSession;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.util.LrdUtils;

public final class FtpDeployerTest {

    private RepositoryBean repositoryBean;
    private LimyWebSession session;
    private FtpDeployer deployer;
    private FTPClient client;
//    private File base;

    @Before
    public void setUp() throws Exception {
        
        FileUtils.deleteDirectory(LrdUtils.getDefaultLocalContentFile("__test__"));
        
        repositoryBean = new RepositoryBean();
        repositoryBean.setId("__test__");
        session = new LimyWebSession(new ArrayList<ScriptSession>());
        
        FtpInfo ftpInfo = new FtpInfo();
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream("FtpDeployerTest.properties"));
        ftpInfo.setServerAddress(prop.getProperty("server"));
        ftpInfo.setUserName(prop.getProperty("user"));
        ftpInfo.setPassword(prop.getProperty("password"));
        ftpInfo.setPath(prop.getProperty("path"));

//        base = new File(prop.getProperty("basePath"), repositoryBean.getId());

        client = new FTPClient();
        FtpUtils.connectFtp(client, ftpInfo);

        deployer = new FtpDeployer(repositoryBean, client, ftpInfo, session);

    }
    
    @After
    public void tearDown() throws IOException {
        FtpUtils.disconnect(client);
   }

    @Test
    public void testDeploy() throws LrdException, IOException {
        deployer.deployLrd("index.lrd", "<html></html>");
    }
    
    @Test
    public void testDeployStatic() throws LrdException, IOException {
        deployer.deployStaticFiles(new File("images"), "images");
//        assertExtstFile("images");
//        assertExtstFile("images/circle.gif");
        
        deployer.deleteDir("images"); // 現在はまだ対応なし
//        assertNotExtstFile("images");
//        assertNotExtstFile("images/circle.gif");

    }


}
