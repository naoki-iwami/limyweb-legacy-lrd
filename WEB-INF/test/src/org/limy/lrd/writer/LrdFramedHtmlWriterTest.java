package org.limy.lrd.writer;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.limy.common.repository.RepositoryInfo;
import org.limy.lrd.LrdCacheManagerImpl;
import org.limy.lrd.LrdMultiModel;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.LrdTargetInfo;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.VelocityEngineModel;
import org.limy.lrd.common.bean.LrdDeployInfo;
import org.limy.lrd.common.bean.LrdNode;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.common.bean.LrdDeployInfo.DeployType;
import org.limy.lrd.parser.LrdParserImpl;
import org.limy.lrd.repository.file.LrdFileRepository;

@RunWith(JMock.class)
public class LrdFramedHtmlWriterTest {

    private Mockery context = new JUnit4Mockery();
    
    private LrdMultiModel lrdMultiModel;

    @Before
    public void setUp() throws Exception {
        
        lrdMultiModel = context.mock(LrdMultiModel.class);

    }
    
    @Test
    public void testWrite() throws Exception {

        final RepositoryBean repositoryBean = new RepositoryBean();
        Collection<LrdDeployInfo> deployInfos = new ArrayList<LrdDeployInfo>();
        deployInfos.add(new LrdDeployInfo(DeployType.LOCAL, "", ""));
        repositoryBean.setDeployInfos(deployInfos);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        LrdFramedHtmlWriterTest.class.getResourceAsStream("sample.lrd")));
        final LrdProject project = new LrdProject("sample");
        
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        project.setRepositoryInfo(repositoryInfo);
        repositoryInfo.setRepositoryUrl("WEB-INF/test/bin/org/limy/lrd/writer"); // TODO これだとallでテスト失敗
        repositoryBean.setRepositoryInfo(repositoryInfo);
        
        LrdPath path = new LrdPath(project, "sample.lrd");
        final LrdRepository repository = new LrdFileRepository(repositoryBean);
        LrdBean bean = new LrdParserImpl().parse(repository, path, reader);
        reader.close();

        Collection<LrdTargetInfo> extInfos = bean.createTargetInfos();
        LrdNode menuRoot = new LrdNode(new LrdPath(project, "sample.lrd"), false);
        LrdFramedHtmlWriter htmlWriter = new LrdFramedHtmlWriter();
//        htmlWriter.setRepository(repository);
        
        final VelocityEngine engine = new VelocityEngine();
        engine.setProperty(Velocity.RESOURCE_LOADER, "class");
        engine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        
        VelocityEngineModel model = new VelocityEngineModel() {
            public VelocityEngine getEngine(RepositoryBean repositoryBean) {
                return engine;
            }
            public void refreshEngine(RepositoryBean repositoryBean) {
            }
        };
        htmlWriter.setVelocityEngineModel(model);
        
        htmlWriter.setModel(lrdMultiModel);
        context.checking(new Expectations() {{
            allowing(lrdMultiModel)
                    .getRepository(with(any(String.class)));
            will(returnValue(repository));
            
            allowing(lrdMultiModel)
                    .getProject(with(any(String.class)));
            will(returnValue(project));
            
            allowing(lrdMultiModel)
                    .getRepositoryBean(with(any(String.class)));
            will(returnValue(repositoryBean));
        }});

        
//        LrdCacheManagerImpl.getInstance().setRepository(repository);
        new LrdCacheManagerImpl();
        LrdCacheManagerImpl.getInstance().setParser(new LrdParserImpl());
        LrdCacheManagerImpl.getInstance().setModel(lrdMultiModel);
        
        htmlWriter.writeHtml(
                repositoryBean,
                bean, new CharArrayWriter(), extInfos, menuRoot,
                new VelocityContext());
        
        CharArrayWriter writer = new CharArrayWriter();
        htmlWriter.writeSimpleHtml(repositoryBean, bean, writer, extInfos, menuRoot);
//        FileUtils.writeByteArrayToFile(
//                new File("WEB-INF/test/src/org/limy/lrd/writer/html/sample.html"),
//                        writer.toString().getBytes("UTF-8"));
        writer.close();

    }

    @Test
    public void testWriteAllMenu() {
//        fail("Not yet implemented");
    }

}
