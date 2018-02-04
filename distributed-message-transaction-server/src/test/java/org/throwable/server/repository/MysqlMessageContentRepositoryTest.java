package org.throwable.server.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.test.context.junit4.SpringRunner;
import org.throwable.server.Application;
import org.throwable.server.model.entity.MessageContent;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 17:46
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MysqlMessageContentRepositoryTest {

    @Autowired
    private MessageContentRepository messageContentRepository;

    @Autowired
    private Environment environment;

    @Test
    public void findByMessageLogId() throws Exception {
        MessageContent messageContent = messageContentRepository.findByMessageLogId(1L);
        Assert.assertNotNull(messageContent);
    }

    @Test
    public void testVipName() throws Exception {
        String applicationName = environment.getProperty("spring.application.name");
        System.out.println(applicationName);
        if (null == applicationName) {
            applicationName = environment.resolvePlaceholders("${project.artifactId}");
        }
        System.out.println(applicationName);
        AbstractEnvironment abstractEnvironment= (AbstractEnvironment) environment;
        MutablePropertySources propertySources = abstractEnvironment.getPropertySources();
        System.out.println(propertySources);

    }
}