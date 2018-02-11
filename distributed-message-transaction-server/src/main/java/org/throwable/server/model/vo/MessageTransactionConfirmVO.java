package org.throwable.server.model.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 23:43
 */
@Data
@ToString
public class MessageTransactionConfirmVO {

	@NotNull(message = "transactionId must not be null!")
	private String transactionId;
	@NotNull(message = "transactionStatus must not be null!")
	private Integer transactionStatus;
}
