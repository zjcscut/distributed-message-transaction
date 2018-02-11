package org.throwable.client.model;

import lombok.Data;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 16:17
 */
@Data
public class CheckLocalTransactionCallback {

    private String transactionId;
    private String businessSign;
    private String applicationName;
    private String instanceSign;
    private String checkQueue;
    private String checkerClassName;
}
