package org.throwable.listener;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/6/22 0:58
 */
public class ConfirmCallbackListener implements RabbitTemplate.ConfirmCallback {

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		System.out.println("ConfirmCallbackListener>>confirm--:correlationData:" + correlationData + ",ack:" + ack + ",cause:" + cause);
	}
}
