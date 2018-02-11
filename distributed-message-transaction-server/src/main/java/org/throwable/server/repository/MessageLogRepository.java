package org.throwable.server.repository;

import org.throwable.server.model.entity.MessageLog;

/**
 * @author throwable
 * @version v1.0
 * @description 消息日志仓库
 * @since 2018/2/2 15:27
 */
public interface MessageLogRepository {

    MessageLog findById(Long id);

    MessageLog findByTransactionId(String transactionId);

    int save(MessageLog messageLog);

    int updateForRegisterConfirm(MessageLog messageLog);

    int updateForTransactionConfirm(MessageLog messageLog);

    int updateForPush(MessageLog messageLog);

    int updateForDelayConfirm(MessageLog messageLog);
}
