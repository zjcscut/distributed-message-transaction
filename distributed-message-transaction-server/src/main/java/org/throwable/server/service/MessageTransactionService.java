package org.throwable.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.throwable.common.CommonConstants;
import org.throwable.server.common.GlobalStatusEnum;
import org.throwable.common.TransactionStatusEnum;
import org.throwable.server.exception.DataPersistenceException;
import org.throwable.server.model.entity.MessageContent;
import org.throwable.server.model.entity.MessageLog;
import org.throwable.server.model.vo.MessageTransactionConfirmVO;
import org.throwable.server.model.vo.MessageTransactionRegisterVO;
import org.throwable.server.repository.MessageContentRepository;
import org.throwable.server.repository.MessageLogRepository;
import org.throwable.server.support.kengen.KeyGenerator;
import org.throwable.utils.AssertUtils;
import org.throwable.utils.BeanCopierUtils;
import org.throwable.utils.JacksonUtils;

import java.util.Date;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 15:17
 */
@Service
public class MessageTransactionService {

    @Autowired
    private MessageLogRepository messageLogRepository;

    @Autowired
    private MessageContentRepository messageContentRepository;

    @Autowired
    private KeyGenerator keyGenerator;

    @Transactional(rollbackFor = Exception.class)
    public MessageLog registerMessageTransaction(MessageTransactionRegisterVO registerVO) {
        MessageLog messageLog = new MessageLog();
        BeanCopierUtils.INSTANCE.copy(registerVO, messageLog);
        messageLog.setId(keyGenerator.generateKey());
        processMessageNullValue(messageLog);
        messageLog.setTransactionStatusEnum(TransactionStatusEnum.REGISTERED);
        messageLog.setGlobalStatusEnum(GlobalStatusEnum.INITIALIZE);
        AssertUtils.INSTANCE.assertThrowRuntimeException(CommonConstants.ONE == messageLogRepository.save(messageLog),
                () -> new DataPersistenceException(String.format("Save message log failed,MessageTransactionRegisterVO = %s",
                        JacksonUtils.INSTANCE.toJson(registerVO))));
        MessageContent content = new MessageContent();
        content.setId(keyGenerator.generateKey());
        content.setMessageLogId(messageLog.getId());
        content.setContent(JacksonUtils.INSTANCE.toJson(registerVO.getPresents()));
        AssertUtils.INSTANCE.assertThrowRuntimeException(CommonConstants.ONE == messageContentRepository.save(content),
                () -> new DataPersistenceException(String.format("Save message content failed,MessageTransactionRegisterVO = %s",
                        JacksonUtils.INSTANCE.toJson(registerVO))));
        return messageLog;
    }

    private void processMessageNullValue(MessageLog messageLog) {
        if (null == messageLog.getCheckQueue()) {
            messageLog.setCheckQueue(CommonConstants.NULL);
        }
        if (null == messageLog.getCheckerClassName()) {
            messageLog.setCheckerClassName(CommonConstants.NULL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public MessageLog confirmMessageTransaction(MessageTransactionConfirmVO messageTransactionConfirmVO) {
        MessageLog messageLog = messageLogRepository.findByTransactionId(messageTransactionConfirmVO.getTransactionId());
        AssertUtils.INSTANCE.assertThrowRuntimeException(null != messageLog,
                () -> new IllegalArgumentException(String.format("Fail to query message log,transactionId = %s",
                        messageTransactionConfirmVO.getTransactionId())));
        messageLog.setTransactionStatusEnum(TransactionStatusEnum.fromValue(messageTransactionConfirmVO.getTransactionStatus()));
        messageLog.setConfirmTime(new Date());
        messageLog.setUpdateTime(new Date());
        messageLogRepository.updateForTransactionConfirm(messageLog);
        return messageLog;
    }
}
