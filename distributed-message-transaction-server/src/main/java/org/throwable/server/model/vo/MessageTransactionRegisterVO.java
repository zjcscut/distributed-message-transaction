package org.throwable.server.model.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 19:02
 */
@ToString
@Data
public class MessageTransactionRegisterVO {

    @NotNull(message = "transactionId must not be null")
    private String transactionId;
    @NotNull(message = "businessSign must not be null")
    private String businessSign;
    @NotNull(message = "applicationName must not be null")
    private String applicationName;
    @NotNull(message = "instanceSign must not be null")
    private String instanceSign;
    @NotNull(message = "triggerQueue must not be null")
    private String triggerQueue;
    private String checkQueue;
    private String checkerClassName;
    @Valid
    @NotNull(message = "presents must not be null!")
    private List<PresentVO> presents;
}
