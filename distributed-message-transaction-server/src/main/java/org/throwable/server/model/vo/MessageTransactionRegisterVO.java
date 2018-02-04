package org.throwable.server.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 19:02
 */
@ToString
@Data
public class MessageTransactionRegisterVO {

    private String transactionId;
    private String businessSign;
    private String applicationName;
    private String instanceSign;
    private String triggerQueue;
    private String checkQueue;
    private String checkerClassName;
}
