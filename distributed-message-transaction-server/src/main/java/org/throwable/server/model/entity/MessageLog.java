package org.throwable.server.model.entity;

import lombok.Data;
import org.throwable.server.common.GlobalStatusEnum;
import org.throwable.common.TransactionStatusEnum;

import java.util.Date;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/2 15:46
 */
@Data
public class MessageLog {

    private Long id;
    private String transactionId;
    private String businessSign;
    private String applicationName;
    private String instanceSign;
    private Integer transactionStatus;
    private Integer globalStatus;
    private String triggerQueue;
    private String checkQueue;
    private String checkerClassName;
    private Date createTime;
    private Date updateTime;
    private Date triggerTime;
    private Date confirmTime;
    private Date pushTime;

    public void setTransactionStatusEnum(TransactionStatusEnum transactionStatusEnum) {
        this.transactionStatus = transactionStatusEnum.ordinal();
    }

    public TransactionStatusEnum getTransactionStatusEnum() {
        for (TransactionStatusEnum status : TransactionStatusEnum.values()) {
            if (null != this.transactionStatus && status.ordinal() == this.transactionStatus) {
                return status;
            }
        }
        return null;
    }

    public void setGlobalStatusEnum(GlobalStatusEnum globalStatusEnum) {
        this.globalStatus = globalStatusEnum.ordinal();
    }

    public GlobalStatusEnum getGlobalStatusEnum() {
        for (GlobalStatusEnum status : GlobalStatusEnum.values()) {
            if (null != this.globalStatus && status.ordinal() == this.globalStatus) {
                return status;
            }
        }
        return null;
    }
}
