package org.limy.lrd.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.limy.common.repository.RepositoryInfo;
import org.limy.common.util.Formatters;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.parser.bean.html.LrdHtmlElementFactory;
import org.limy.lrd.repository.file.LrdFileRepository;

public class LrdLinkElementTest {
    
    private static File REPOSITORY_DIR;

    private LrdBlockElement rootElement = new LrdHtmlElementFactory().createBlockElement(null, 0);

    private LrdProject project;

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
        
        project = new LrdProject("projectId");
        repositoryBean = new RepositoryBean();
        repositoryBean.setRepository(new LrdFileRepository(repositoryBean));
        
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setRepositoryUrl(REPOSITORY_DIR.getAbsolutePath());
        project.setRepositoryInfo(repositoryInfo);
        repositoryBean.setRepositoryInfo(repositoryInfo);
//        repositoryInfo.setRepositoryUrl(REPOSITORY_DIR.getAbsolutePath());
//        repositoryBean.setRepositoryInfo(repositoryInfo);

    }
    
    @Test
    public void testGetLinkText() throws IOException {
        
        final LrdBean bean = new LrdBean(rootElement, new LrdPath(project, "dir1/index.lrd"));
        rootElement.setBean(bean);

        LrdLinkElement el1 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "sample");
        LrdLinkElement el2 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "\"sample\"");
        LrdLinkElement el3 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "URL:other.html");
        LrdLinkElement el4 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "sample", "view sample");
        LrdLinkElement el5 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "URL:other.html", "oo");
        LrdLinkElement el6 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "IMG:top.png");
        LrdLinkElement el7 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "IMG:top.png", "img");
        LrdLinkElement el8 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "URL:other.html");
        el8.setShowDate(true);
        LrdLinkElement el9 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "URL:notfile.html");
        el9.setShowDate(true);
        LrdLinkElement el10 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "notref");
        LrdLinkElement el11 = new LrdLinkElement(
                repositoryBean.getRepository(), rootElement, "URL:http://other.org/");

        LrdBean beanRef1 = new LrdBean(rootElement, new LrdPath(project, "dir2/sample.lrd"));

        // dir1/index.lrd から　dir2/index.lrd#12 を参照
        Collection<LrdTargetInfo> infos = new ArrayList<LrdTargetInfo>();
        infos.add(new LrdTargetInfo("sample", beanRef1, "12"));
        
        el1.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a href=\"../dir2/sample.html#12\">sample</a>", el1.getLinkText());
        
        el2.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a href=\"../dir2/sample.html#12\">sample</a>", el2.getLinkText());

        el3.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a href=\"other.html\">other.html</a>", el3.getLinkText());

        el4.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a href=\"../dir2/sample.html#12\">view sample</a>", el4.getLinkText());

        el5.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a href=\"other.html\">oo</a>", el5.getLinkText());

        el6.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<img src=\"top.png\" alt=\"top.png\" />", el6.getLinkText());

        el7.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<img src=\"top.png\" alt=\"img\" />", el7.getLinkText());

        FileUtils.touch(new File(REPOSITORY_DIR, "dir1/other.lrd"));
        el8.decideDynamicTarget(bean, infos);
        // <a href="other.html">other.html</a>　<font size='-1'>(2007/08/17)</font>
        Assert.assertTrue(el8.getLinkText().indexOf(
                Formatters.dateFormat(Formatters.D_VIEW_8, new Date())) >= 0);

        el9.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a href=\"notfile.html\">notfile.html</a>　<font color=\"red\">Not Found!</font>", el9.getLinkText());

        el10.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<strong>!!notref!!</strong>", el10.getLinkText());

        el11.decideDynamicTarget(bean, infos);
        Assert.assertEquals("<a class=\"externalLink\" title=\"External Link\" href=\"http://other.org/\">http://other.org/</a>", el11.getLinkText());

        
    }

}
