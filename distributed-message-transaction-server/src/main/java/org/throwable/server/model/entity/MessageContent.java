package org.throwable.server.model.entity;

import lombok.Data;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:48
 */
@Data
public class MessageContent {

    private Long id;
    private Long messageLogId;
    private String content;
}
