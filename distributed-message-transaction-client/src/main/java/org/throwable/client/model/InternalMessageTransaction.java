package org.throwable.client.model;

import lombok.Data;

import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:23
 */
@Data
public class InternalMessageTransaction {

    private String transactionId;
    private String businessSign;
    private String applicationName;
    private String instanceSign;
    private String triggerQueue;
    private String checkQueue;
    private String checkerClassName;
    private List<DestinationMessage> presents;
}
