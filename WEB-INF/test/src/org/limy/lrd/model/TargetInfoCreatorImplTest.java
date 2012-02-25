package org.limy.lrd.model;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.limy.common.repository.RepositoryInfo;
import org.limy.common.svn.CommitFileInfo;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.common.LrdCacheManager;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.parser.bean.html.LrdHtmlElementFactory;
import org.limy.lrd.repository.file.LrdFileRepository;

@RunWith(JMock.class)
public class TargetInfoCreatorImplTest {

    private static File REPOSITORY_DIR;

    private Mockery context = new JUnit4Mockery();

    private LrdBlockElement rootElement = new LrdHtmlElementFactory().createBlockElement(null, 0);

    private LrdProject project;

    private RepositoryBean repositoryBean;

    private LrdCacheManager cacheManager;

    @BeforeClass
    public static void setUpOnce() throws IOException {
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("test-common.properties"));
        REPOSITORY_DIR = new File(prop.getProperty("repository.dir"));
    }
    
    @Before
    public void setUp() throws Exception {
        
        cacheManager = context.mock(LrdCacheManager.class);

        
        project = new LrdProject("projectId");
        repositoryBean = new RepositoryBean();
        repositoryBean.setRepository(new LrdFileRepository(repositoryBean));
        
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setRepositoryUrl(REPOSITORY_DIR.getAbsolutePath());
        repositoryBean.setRepositoryInfo(repositoryInfo);

    }
    
    @Test
    public void testCreateTargetInfos() throws LrdException, IOException {
        
        TargetInfoCreatorImpl creator = new TargetInfoCreatorImpl();
        creator.setCacheManager(cacheManager);
        
        final LrdBean bean = new LrdBean(rootElement, new LrdPath(project, "dir1/index.lrd"));
        bean.setRef("../dir2,../dir3,../sample.lrd");
        new File(REPOSITORY_DIR, "dir2").mkdirs();
        new File(REPOSITORY_DIR, "dir3").mkdirs();
        FileUtils.touch(new File(REPOSITORY_DIR, "sample.lrd"));

        context.checking(new Expectations() {{
            allowing(cacheManager)
                    .getBean(with(any(String.class)), with(any(CommitFileInfo.class)));
            will(returnValue(bean));
        }});

        Collection<LrdTargetInfo> infos = creator.createTargetInfos(repositoryBean, bean);
        assertNotNull(infos);
        
    }

}
