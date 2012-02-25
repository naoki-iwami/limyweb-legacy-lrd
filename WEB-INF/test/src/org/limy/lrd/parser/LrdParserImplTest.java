package org.limy.lrd.parser;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.Test;
import org.limy.lrd.common.LrdBean;
import org.limy.lrd.common.LrdRepository;
import org.limy.lrd.common.RepositoryBean;
import org.limy.lrd.common.bean.LrdPath;
import org.limy.lrd.common.bean.LrdProject;
import org.limy.lrd.repository.file.LrdFileRepository;

public class LrdParserImplTest {

    @Test
    public void testParse() throws Exception {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        LrdParserImplTest.class.getResourceAsStream("sample.lrd")));
        LrdProject project = new LrdProject("sample");
        LrdPath path = new LrdPath(project, "/sample");
        RepositoryBean repositoryBean = new RepositoryBean();
        LrdRepository repository = new LrdFileRepository(repositoryBean);
        LrdBean bean = new LrdParserImpl().parse(repository, path, reader);
        assertNotNull(bean);

        reader.close();
    }

}
