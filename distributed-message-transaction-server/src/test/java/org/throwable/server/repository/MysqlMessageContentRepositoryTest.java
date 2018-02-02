package org.throwable.server.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.throwable.server.Application;
import org.throwable.server.model.entity.MessageContent;

import static org.junit.Assert.*;

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

    @Test
    public void findByMessageLogId() throws Exception {
        MessageContent messageContent = messageContentRepository.findByMessageLogId(1L);
        Assert.assertNotNull(messageContent);
    }
}