package org.throwable.client.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 23:20
 */
@ToString
@Data
public class ConfirmLocalTransactionCallback {

	private String transactionId;
	private String businessSign;
	private String applicationName;
	private String instanceSign;
	private String triggerQueue;
}
