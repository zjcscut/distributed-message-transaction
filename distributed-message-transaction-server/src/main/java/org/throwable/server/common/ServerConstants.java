package org.throwable.server.common;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/4 23:53
 */
public class ServerConstants {

	public static final String REGISTER_QUEUE_NAME = "dmt.server.transaction.register";

	public static final String REGISTER_EXCHANGE_NAME = REGISTER_QUEUE_NAME;

	public static final String REGISTER_ROUTING_KEY = REGISTER_QUEUE_NAME;

	public static final String CONFIRM_QUEUE_NAME = "dmt.server.transaction.confirm";

	public static final String CONFIRM_EXCHANGE_NAME = CONFIRM_QUEUE_NAME;

	public static final String CONFIRM_ROUTING_KEY = CONFIRM_QUEUE_NAME;
}
