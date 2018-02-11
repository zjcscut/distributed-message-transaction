package org.throwable.server.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 17:23
 */
@Data
@ToString
public class RegisterConfirmCallbackDTO {

    private String transactionId;
    private String businessSign;
    private String applicationName;
    private String instanceSign;
    private String triggerQueue;
}
