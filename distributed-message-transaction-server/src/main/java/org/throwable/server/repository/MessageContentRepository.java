package org.throwable.server.repository;

import org.throwable.server.model.entity.MessageContent;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:28
 */
public interface MessageContentRepository {

    MessageContent findByMessageLogId(Long id);

    int save(MessageContent messageContent);
}
