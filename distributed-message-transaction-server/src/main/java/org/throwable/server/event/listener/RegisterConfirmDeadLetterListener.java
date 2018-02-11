package org.throwable.server.event.listener;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.throwable.common.CommonConstants;
import org.throwable.server.common.ServerConstants;
import org.throwable.server.event.category.RegisterSuccessEvent;
import org.throwable.server.model.vo.MessageTransactionDelayConfirmVO;
import org.throwable.utils.JacksonUtils;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2018/2/5 17:32
 */
public class RegisterConfirmDeadLetterListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @EventListener
    public void listen(RegisterSuccessEvent event) throws Exception {
        MessageTransactionDelayConfirmVO vo = new MessageTransactionDelayConfirmVO();
        vo.setId(event.getMessageLog().getId());
        rabbitTemplate.send(ServerConstants.DELAY_CONFIRM_DEAD_LETTER_QUEUE, ServerConstants.DELAY_CONFIRM_DEAD_LETTER_QUEUE,
                MessageBuilder.withBody(JacksonUtils.INSTANCE.toJson(vo).getBytes(CommonConstants.CHARSET)).build());
    }
}
