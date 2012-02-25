package org.limy.lrd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.limy.common.repository.RepositoryInfo;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdBlockElement;
import org.limy.lrd.common.LrdException;
import org.limy.lrd.common.LrdLinkElement;
import org.limy.lrd.common.LrdParser;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.parser.bean.LrdMultiTextElement;
import org.limy.lrd.parser.bean.html.LrdHtmlElementFactory;
import org.limy.lrd.repository.file.LrdFileRepository;

@RunWith(JMock.class)
public final class LrdCacheManagerImplTest {
    
    private static final String REPOSITORY_ID = "REP";
    
    private static File REPOSITORY_DIR;
    
    private LrdBlockElement rootElement = new LrdHtmlElementFactory().createBlockElement(null, 0);

    private Mockery context = new JUnit4Mockery();
    
    private LrdMultiModel lrdMultiModel;

    private LrdCacheManagerImpl instance;
    
    private LrdParser parser;
    
    private LrdProject project;

    private LrdRepository repository;

    private RepositoryBean repositoryBean;

    @BeforeClass
    public static void setUpOnce() throws IOException {
        Properties prop = new Properties();
        prop.load(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("test-common.properties"));
        REPOSITORY_DIR = new File(prop.getProperty("repository.dir"));
    }
    
    @Before
    public void setUp() throws Exception {
        
        
        lrdMultiModel = context.mock(LrdMultiModel.class);
        parser = context.mock(LrdParser.class);
        
        instance = new LrdCacheManagerImpl();
        instance.setModel(lrdMultiModel);
        instance.setParser(parser);
        
        project = new LrdProject("projectId");
        
        repositoryBean = new RepositoryBean();
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setRepositoryUrl(REPOSITORY_DIR.getAbsolutePath());
        repositoryBean.setRepositoryInfo(repositoryInfo);
        
        repository = new LrdFileRepository(repositoryBean);
        
    }

    @Test
    public void testGetIndexLrdTitle() throws IOException {
        
        context.checking(new Expectations() {{
            allowing(lrdMultiModel)
                    .getRepository(with(any(String.class)));
            will(returnValue(repository));
            allowing(lrdMultiModel)
                    .getProject(with(any(String.class)));
            will(returnValue(project));
            
            allowing(parser)
                    .parse(
                            with(any(LrdRepository.class)),
                            with(any(LrdPath.class)),
                            with(any(BufferedReader.class)));
//            LrdBlockElement rootElement = new LrdHtmlElementFactory().createBlockElement(null, 0);
            LrdBean bean = new LrdBean(rootElement, new LrdPath(project, "dir1/index.lrd"));
            bean.setTitle("lrd title");
            will(returnValue(bean));
            
        }});

        File indexFile = new File(REPOSITORY_DIR, "dir1/index.lrd");
        indexFile.getParentFile().mkdirs();
        FileUtils.writeStringToFile(indexFile, "title=abc\n");
        
        // dir/index.lrd にタイトルが設定してある場合
        String title = instance.getIndexLrdTitle(REPOSITORY_ID, new LrdPath(project, "dir1"));
        Assert.assertEquals("lrd title", title);
        
        // 2回目はキャッシュから取る（カバレッジの為）
        instance.getIndexLrdTitle(REPOSITORY_ID, new LrdPath(project, "dir1"));
        
        instance.clearLrdCache(REPOSITORY_ID, "dir1/index.lrd");

    }

    @Test
    public void testGetIndexLrdTitle_NotTitle() throws IOException {
        
        context.checking(new Expectations() {{
            allowing(lrdMultiModel)
                    .getRepository(with(any(String.class)));
            will(returnValue(repository));
            allowing(lrdMultiModel)
                    .getProject(with(any(String.class)));
            will(returnValue(project));
            
            allowing(parser)
                    .parse(
                            with(any(LrdRepository.class)),
                            with(any(LrdPath.class)),
                            with(any(BufferedReader.class)));
//            LrdBlockElement rootElement = new LrdHtmlElementFactory().createBlockElement(null, 0);
            LrdBean bean = new LrdBean(rootElement, new LrdPath(project, "dir2/index.lrd"));
            will(returnValue(bean));
            
        }});

        File indexFile = new File(REPOSITORY_DIR, "dir2/index.lrd");
        indexFile.getParentFile().mkdirs();
        FileUtils.writeStringToFile(indexFile, "title=abc\n");
        
        // dir/index.lrd にタイトルが設定していない場合、ディレクトリ名をそのまま
        String title = instance.getIndexLrdTitle(REPOSITORY_ID, new LrdPath(project, "dir2"));
        Assert.assertEquals("dir2", title);

    }

    @Test
    public void testGetIndexLrdTitle_NotTitle_ROOT() throws IOException {
        
        context.checking(new Expectations() {{
            allowing(lrdMultiModel)
                    .getRepository(with(any(String.class)));
            will(returnValue(repository));
            allowing(lrdMultiModel)
                    .getProject(with(any(String.class)));
            will(returnValue(project));
            
            allowing(parser)
                    .parse(
                            with(any(LrdRepository.class)),
                            with(any(LrdPath.class)),
                            with(any(BufferedReader.class)));
            LrdBean bean = new LrdBean(rootElement, new LrdPath(project, ""));
            will(returnValue(bean));
            
        }});

        File indexFile = new File(REPOSITORY_DIR, "index.lrd");
        indexFile.getParentFile().mkdirs();
        FileUtils.writeStringToFile(indexFile, "title=abc\n");
        
        // ルートの index.lrd にタイトルが設定していない場合、*ROOT* とする
        String title = instance.getIndexLrdTitle(REPOSITORY_ID, new LrdPath(project, ""));
        Assert.assertEquals("*ROOT*", title);

    }

    @Test
    public void testClearMenuTreeCache() throws IOException {
        instance.clearMenuTreeCache(REPOSITORY_ID);
    }
    
    @Test
    public void testGetMenuTree() throws IOException, LrdException {
        
        context.checking(new Expectations() {{
            allowing(lrdMultiModel)
                    .getRepository(with(any(String.class)));
            will(returnValue(repository));
        }});

        final LrdBean bean = new LrdBean(rootElement, new LrdPath(project, "dir5/index.lrd"));
        
        // リンクを作成
        LrdMultiTextElement el1 = new LrdMultiTextElement(bean, rootElement);
        el1.appendElement(new LrdLinkElement(repository, rootElement, "URL:index.html"));
        rootElement.appendElement(el1);

        context.checking(new Expectations() {{
            allowing(lrdMultiModel)
                    .getProject(with(any(String.class)));
            will(returnValue(project));
            
            allowing(parser)
            .parse(
                    with(any(LrdRepository.class)),
                    with(any(LrdPath.class)),
                    with(any(BufferedReader.class)));
            will(returnValue(bean));
        }});

        LrdNode root = instance.getMenuTree(REPOSITORY_ID, bean);
        
        // dir5/index.lrd を表示時は、dir2 は表示する
        Assert.assertTrue(root.searchSubNodeDepth("dir2").isEnable());
        // dir5/index.lrd を表示時は、dir2/... は表示しない
        Assert.assertFalse(root.searchSubNodeDepth("dir2/index.lrd").isEnable());
    }

    
    

}
