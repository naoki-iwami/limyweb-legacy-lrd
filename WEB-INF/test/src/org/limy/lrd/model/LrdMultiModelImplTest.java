package org.limy.lrd.model;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.LrdCompileModel;
import org.limy.lrd.LrdConfig;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.parser.bean.html.LrdHtmlElementFactory;

@RunWith(JMock.class)
public class LrdMultiModelImplTest {

    private static final String REPOSITORY1 = "r1";
    
    private static File BASE_DIR;
    
    private LrdMultiModelImpl model;
    
    private Mockery context = new JUnit4Mockery();
    private LrdConfig config;
    private LrdRepository lrdRepository;
    private LrdCacheManager cacheManager;
    private LrdCompileModel compileModel;
    private RepositoryBean repositoryBean;

    @BeforeClass
    public static void setUpOnce() throws IOException {
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("test-common.properties"));
        BASE_DIR = new File(prop.getProperty("tmp.file.dir"));
    }

    @Before
    public void setUp() throws Exception {
        
        FileUtils.deleteDirectory(BASE_DIR);
        
        model = new LrdMultiModelImpl();
        
        config = context.mock(LrdConfig.class);
        model.setConfig(config);
        
        cacheManager = context.mock(LrdCacheManager.class);
        model.setCacheManager(cacheManager);

        compileModel = context.mock(LrdCompileModel.class);
        model.setCompileModel(compileModel);

        lrdRepository = context.mock(LrdRepository.class);
        
        repositoryBean = new RepositoryBean();
        repositoryBean.setRepository(lrdRepository);
        
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(BASE_DIR);
    }
    
    @Test
    public void testCreateFile() throws Exception {
        
        context.checking(new Expectations() {{
            
            allowing(config).getRepositoryBean(REPOSITORY1);
            will(returnValue(repositoryBean));
            
            allowing(lrdRepository).addFile(
                    with(any(String.class)));
            allowing(cacheManager).clearMenuTreeCache(REPOSITORY1);
        }});
        model.createFile(REPOSITORY1, "test/index.lrd");
        
        context.checking(new Expectations() {{
            allowing(compileModel).compile(
                    with(any(String.class)),
                    with(any(String.class)),
                    with(any(String.class)));
            allowing(cacheManager).clearLrdCache(
                    with(any(String.class)),
                    with(any(String.class))
            );
            allowing(cacheManager).createBean(
                    with(any(String.class)),
                    with(any(String.class)),
                    with(any(String.class)),
                    with(any(Boolean.class))
            );
            will(returnValue(new LrdBean(
                    new LrdHtmlElementFactory().createBlockElement(null, 0),
                    new LrdPath(new LrdProject(""), ""))));
            
            allowing(lrdRepository).commitMultiFile(
                    new CommitFileInfo[] { with(any(CommitFileInfo.class)) });

        }});
        model.commit(REPOSITORY1, "test/index.lrd", "=begin\nok.\n=end");
        
        context.checking(new Expectations() {{
            allowing(lrdRepository).getRepositoryFile(
                    with(any(String.class)));
            will(returnValue(new CommitFileInfo("test/index.lrd", "".getBytes(), "EUC-JP")));

        }});
        model.changeLocation(REPOSITORY1, "test/index.lrd");
        //Assert.assertEquals("test/index.html", resource.getHtmlUrl());
        
        model.deploy(REPOSITORY1, "test/index.lrd", "test");
    }


}
