package org.throwable.server.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.throwable.common.CommonConstants;
import org.throwable.common.TransactionStatusEnum;
import org.throwable.server.common.GlobalStatusEnum;
import org.throwable.server.event.category.TransactionDelayConfirmEvent;
import org.throwable.server.model.dto.CheckConfirmCallbackDTO;
import org.throwable.server.model.entity.MessageContent;
import org.throwable.server.model.entity.MessageLog;
import org.throwable.server.model.vo.PresentVO;
import org.throwable.server.repository.MessageContentRepository;
import org.throwable.server.repository.MessageLogRepository;
import org.throwable.server.support.ServerRabbitComponentManager;
import org.throwable.utils.JacksonUtils;

import java.util.Date;
import java.util.List;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/6 12:30
 */
@Slf4j
public class TransactionDelayConfirmListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageLogRepository messageLogRepository;

    @Autowired
    private MessageContentRepository messageContentRepository;

    @Autowired
    private ServerRabbitComponentManager serverRabbitComponentManager;

    @EventListener
    public void listen(TransactionDelayConfirmEvent event) throws Exception {
        MessageLog messageLog = messageLogRepository.findById(event.getId());
        TransactionStatusEnum transactionStatusEnum = messageLog.getTransactionStatusEnum();
        //如果事务状态不是终结状态
        if (!TransactionStatusEnum.isTerminal(transactionStatusEnum)) {
            if (!CommonConstants.NULL.equals(messageLog.getCheckQueue()) && !CommonConstants.NULL.equals(messageLog.getCheckerClassName())) {
                Boolean queueExisted = rabbitTemplate.execute(channel -> {
                    try {
                        channel.queueDeclarePassive(messageLog.getCheckQueue());
                        return Boolean.TRUE;
                    } catch (Exception e) {
                        //ignore
                    }
                    return Boolean.FALSE;
                });
                if (Boolean.TRUE.equals(queueExisted)) {
                    CheckConfirmCallbackDTO dto = new CheckConfirmCallbackDTO();
                    dto.setTransactionId(messageLog.getTransactionId());
                    dto.setBusinessSign(messageLog.getBusinessSign());
                    dto.setApplicationName(messageLog.getApplicationName());
                    dto.setInstanceSign(messageLog.getInstanceSign());
                    dto.setCheckQueue(messageLog.getCheckQueue());
                    dto.setCheckerClassName(messageLog.getCheckerClassName());
                    rabbitTemplate.send(messageLog.getCheckQueue(), messageLog.getCheckQueue(),
                            MessageBuilder.withBody(JacksonUtils.INSTANCE.toJson(dto).getBytes(CommonConstants.CHARSET)).build());
                    messageLog.setGlobalStatusEnum(GlobalStatusEnum.CHECK);
                } else {
                    if (log.isWarnEnabled()) {
                        log.warn("Miss check local transaction status for CheckQueue is not existed in the broker,transactionId [{}]",messageLog.getTransactionId());
                    }
                    messageLog.setTransactionStatusEnum(TransactionStatusEnum.FAIL);
                    messageLog.setGlobalStatusEnum(GlobalStatusEnum.MISS_CHECK);
                }
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Miss check local transaction status for 'CheckQueue' or 'CheckerClassName' is null,transactionId [{}]",messageLog.getTransactionId());
                }
                messageLog.setTransactionStatusEnum(TransactionStatusEnum.FAIL);
                messageLog.setGlobalStatusEnum(GlobalStatusEnum.MISS_CHECK);
            }
            messageLog.setUpdateTime(new Date());
            messageLogRepository.updateForDelayConfirm(messageLog);
        } else {
            //如果事务状态已经是终结状态、未推送消息并且事务是成功的
			//TODO 这里有可能出现一个问题：当需要发送的消息列表多于一个的时候，有可能出现部分成功，部分失败的可能
			//解决方案：
			//(1)使用rabbitmq的事务，不过性能是比较差的
			//(2)考虑每条远端的消息用单独一行来存放，这样可能明确知道哪条推送成功，哪条推送失败的
            if (!GlobalStatusEnum.PUSH.equals(messageLog.getGlobalStatusEnum()) && TransactionStatusEnum.SUCCESS.equals(transactionStatusEnum)) {
                MessageContent messageContent = messageContentRepository.findByMessageLogId(messageLog.getId());
                List<PresentVO> presents = JacksonUtils.INSTANCE.parseList(messageContent.getContent(), PresentVO.class);
                if (null != presents && !presents.isEmpty()) {
                    for (PresentVO present : presents) {
                        Boolean queueExisted = rabbitTemplate.execute(channel -> {
                            try {
                                channel.queueDeclarePassive(present.getDestination());
                                return Boolean.TRUE;
                            } catch (Exception e) {
                                //ignore
                            }
                            return Boolean.FALSE;
                        });
                        if (Boolean.FALSE.equals(queueExisted)) {
                            serverRabbitComponentManager.declareDirectQueue(present.getDestination());
                        }
                        rabbitTemplate.send(present.getDestination(), present.getDestination(),
                                MessageBuilder.withBody(present.getContent().getBytes(CommonConstants.CHARSET)).build());
                    }
                    messageLog.setPushTime(new Date());
                    messageLog.setUpdateTime(new Date());
                    messageLog.setGlobalStatusEnum(GlobalStatusEnum.PUSH);
                    messageLogRepository.updateForPush(messageLog);
                }
            }
        }
    }
}
