package org.throwable.server.common;

/**
 * @author throwable
 * @version v1.0
 * @description 消息日志全局状态
 * @since 2018/2/4 15:06
 */
public enum GlobalStatusEnum {

    /**
     * 初始化状态
     */
    INITIALIZE,

    /**
     * 已触发事务
     */
    TRIGGER,

    /**
     * 已触发事务检查
     */
    CHECK,

    /**
     * 已进行远程消息推送
     */
    PUSH

}
