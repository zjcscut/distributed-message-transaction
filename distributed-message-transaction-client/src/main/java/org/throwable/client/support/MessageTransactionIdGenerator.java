package org.throwable.client.support;

import java.util.UUID;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/11 23:33
 */
public class MessageTransactionIdGenerator {

	public String generateTransactionId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
