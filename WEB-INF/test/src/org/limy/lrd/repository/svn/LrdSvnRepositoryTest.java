package org.limy.lrd.repository.svn;

import java.io.File;
import java.io.Writer;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.limy.common.repository.RepositoryInfo;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.common.LrdWriter;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.tmatesoft.svn.core.io.SVNRepository;

@RunWith(JMock.class)
public class LrdSvnRepositoryTest {
    
    /** svnserveパス TODO 環境依存 */
    private static final String SVNSERVE_EXEC = "C:/usr/local/Subversion/bin/svnserve.exe";

    /** svnadminパス TODO 環境依存 */
    private static final String SVNADMIN_EXEC = "C:/usr/local/Subversion/bin/svnadmin.exe";

    /** SVNパス TODO 環境依存 */
    private static final String SVN_URL = "svn://localhost/var/tmp/svn/";
    
    private static final String SVN_USER = "guest";
    
    private static final String SVN_PASS = "guest";
    
    private static File SVN_DIR;
    
    private static Process process;

    // ------------------------ Fields

    private Mockery context = new JUnit4Mockery();
    private LrdWriter writer;
    private RepositoryBean repositoryBean;
    
    @BeforeClass
    public static void setUpOnce() throws Exception {
        
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("test-common.properties"));
        SVN_DIR = new File(prop.getProperty("tmp.svn.dir"));

        System.out.println("start svnserve...");
        
        ProcessBuilder builder = new ProcessBuilder(SVNSERVE_EXEC, "-d");
        builder.redirectErrorStream(true);
        process = builder.start();
        Thread.sleep(1000);
    }
    
    @AfterClass
    public static void tearDownOnce() throws Exception {
        System.out.println("stop svnserve.");
        process.destroy();
    }
    
    @Before
    public void setUp() throws Exception {
        
        FileUtils.deleteDirectory(SVN_DIR);

        System.out.println("setup...");
        SVN_DIR.mkdirs();
        
        ProcessBuilder builder = new ProcessBuilder(SVNADMIN_EXEC,
                "create", SVN_DIR.getAbsolutePath());
        builder.redirectErrorStream(true);
        Process process = builder.start();
        process.waitFor();
        
        FileUtils.writeStringToFile(new File(SVN_DIR, "conf/svnserve.conf"),
                "[general]\npassword-db = passwd",
                null);
        FileUtils.writeStringToFile(new File(SVN_DIR, "conf/passwd"),
                "[users]\nguest = guest",
                null);
        System.out.println("setup ok.");
        
        writer = context.mock(LrdWriter.class);
        
        repositoryBean = new RepositoryBean();
        repositoryBean.setUrl(SVN_URL);
        repositoryBean.setUserName(SVN_USER);
        repositoryBean.setPassword(SVN_PASS);
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(SVN_DIR);
    }

    @Test
    public void testAll() throws Exception {
        
        LrdSvnRepository repository = new LrdSvnRepository(repositoryBean);
        
        LrdProject project = new LrdProject("sample");

        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setAuthUser(SVN_USER);
        repositoryInfo.setAuthPass(SVN_PASS);
        repositoryInfo.setRepositoryUrl(SVN_URL);
        project.setRepositoryInfo(repositoryInfo);

        LrdNode root = repository.getDirectoryRoot();
        Assert.assertNotNull(root);
        
        repository.addFile("sample1.lrd");
        repository.addFile("sample2.lrd");
        repository.addFile("sample3.lrd");
        repository.addFile("sample/sub1.lrd");
        repository.addFile("sample/sub2.lrd");
        repository.addFile("1/2/3.lrd");
        
        CommitFileInfo file = repository.getRepositoryFile("sample1.lrd");
        Assert.assertNotNull(file);
        
        CommitFileInfo[] files = repository.getRepositoryFiles(
                new String[] { "sample1.lrd", "sample2.lrd" });
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
        
        LrdPath[] lrdPaths = repository.getFilesFromDirectory("");
        Assert.assertNotNull(lrdPaths);
        Assert.assertEquals(3, lrdPaths.length);
        
        Assert.assertFalse(repository.isDirectory("sample1.lrd"));
        
        SVNRepository svnRepository = repository.getSvnRepository();
        Assert.assertNotNull(svnRepository);
        
        Date timestamp = repository.getTimestamp("", SVN_USER, SVN_PASS);
        Assert.assertNotNull(timestamp);
        
        context.checking(new Expectations() {{
            allowing(writer).writeAllMenu(
                    with(any(RepositoryBean.class)), 
                    with(any(Writer.class)), with(any(LrdNode.class)));
        }});
        repository.getAllMenu(writer);
        repository.getAllMenu(writer); // 2回目はキャッシュ使用
        
        CommitFileInfo[] infos = new CommitFileInfo[] {
                new CommitFileInfo("sample3.lrd", "SAMPLE".getBytes(), "EUC-JP"),
        };
        repository.commitMultiFile(infos);
        

        file = repository.getRepositoryFile("sample3.lrd");
        Assert.assertEquals("SAMPLE", file.getContentStr());
    }

}
