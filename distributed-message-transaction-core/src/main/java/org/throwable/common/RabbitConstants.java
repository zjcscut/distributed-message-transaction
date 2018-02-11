package org.throwable.common;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 9:50
 */
public class RabbitConstants {

    public static final String REGISTER_QUEUE_NAME = "dmt.server.transaction.register";

    public static final String REGISTER_EXCHANGE_NAME = REGISTER_QUEUE_NAME;

    public static final String REGISTER_ROUTING_KEY = REGISTER_QUEUE_NAME;

    public static final String CONFIRM_QUEUE_NAME = "dmt.server.transaction.confirm";

    public static final String CONFIRM_EXCHANGE_NAME = CONFIRM_QUEUE_NAME;

    public static final String CONFIRM_ROUTING_KEY = CONFIRM_QUEUE_NAME;

    public static final String TRIGGER_QUEUE_NAME = "dmt.client.transaction.trigger";

    public static final String CHECK_QUEUE_NAME = "dmt.client.transaction.check";
}
