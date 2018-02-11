package org.throwable.server.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.throwable.common.CommonConstants;
import org.throwable.common.TransactionStatusEnum;
import org.throwable.server.common.GlobalStatusEnum;
import org.throwable.server.event.category.RegisterSuccessEvent;
import org.throwable.server.model.dto.RegisterConfirmCallbackDTO;
import org.throwable.server.model.entity.MessageLog;
import org.throwable.server.repository.MessageLogRepository;
import org.throwable.utils.BeanCopierUtils;
import org.throwable.utils.JacksonUtils;

import java.util.Date;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 17:19
 */
@Slf4j
public class RegisterConfirmCallbackListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageLogRepository messageLogRepository;

    @EventListener
    public void listen(RegisterSuccessEvent event) throws Exception {
        MessageLog messageLog = event.getMessageLog();
        RegisterConfirmCallbackDTO callback = new RegisterConfirmCallbackDTO();
        BeanCopierUtils.INSTANCE.copy(messageLog, callback);
        Boolean queueExisted = rabbitTemplate.execute(channel -> {
            try {
                channel.queueDeclarePassive(messageLog.getTriggerQueue());
                return Boolean.TRUE;
            } catch (Exception e) {
                //ignore
            }
            return Boolean.FALSE;
        });
        if (Boolean.TRUE.equals(queueExisted)) {
            rabbitTemplate.send(messageLog.getTriggerQueue(), messageLog.getTriggerQueue(),
                    MessageBuilder.withBody(JacksonUtils.INSTANCE.toJson(callback).getBytes(CommonConstants.CHARSET)).build());
            messageLog.setGlobalStatusEnum(GlobalStatusEnum.TRIGGER);
            messageLog.setTriggerTime(new Date());
            messageLog.setUpdateTime(new Date());
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Miss trigger queue [{}] to confirm transaction registered,target queue is not existed in the broker", messageLog.getTriggerQueue());
            }
            messageLog.setGlobalStatusEnum(GlobalStatusEnum.MISS_TRIGGER);
            messageLog.setTriggerTime(new Date());
            messageLog.setUpdateTime(new Date());
            messageLog.setTransactionStatusEnum(TransactionStatusEnum.FAIL);
        }
        messageLogRepository.updateForRegisterConfirm(messageLog);
    }
}
