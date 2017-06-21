package org.throwable.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.throwable.entity.MessageTarget;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/6/22 1:03
 */
@Service
public class PublishService {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(String exchange, String routingKey, MessageTarget target) {
		CorrelationData correlationData = new CorrelationData(target.getId());
		rabbitTemplate.convertAndSend(exchange, routingKey, target, correlationData);
	}
}
