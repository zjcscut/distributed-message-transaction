package org.throwable.server.event.listener;

import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.throwable.common.CommonConstants;
import org.throwable.common.TransactionStatusEnum;
import org.throwable.server.common.GlobalStatusEnum;
import org.throwable.server.event.category.TransactionConfirmEvent;
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
public class TransactionConfirmListener {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private MessageLogRepository messageLogRepository;

	@Autowired
	private MessageContentRepository messageContentRepository;

	@Autowired
	private ServerRabbitComponentManager serverRabbitComponentManager;

	@EventListener
	public void listen(TransactionConfirmEvent event) throws Exception {
		MessageLog messageLog = event.getMessageLog();
		MessageContent messageContent = messageContentRepository.findByMessageLogId(messageLog.getId());
		if (!GlobalStatusEnum.PUSH.equals(messageLog.getGlobalStatusEnum()) && TransactionStatusEnum.SUCCESS.equals(messageLog.getTransactionStatusEnum())) {
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
			}
			messageLog.setPushTime(new Date());
			messageLog.setUpdateTime(new Date());
			messageLog.setGlobalStatusEnum(GlobalStatusEnum.PUSH);
			messageLogRepository.updateForPush(messageLog);
		}
	}
}
