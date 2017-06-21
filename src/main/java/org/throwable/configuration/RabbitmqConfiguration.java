package org.throwable.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.throwable.listener.ConfirmCallbackListener;
import org.throwable.listener.ReturnCallbackListener;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/6/22 0:47
 */
@Configuration
public class RabbitmqConfiguration {

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory("localhost", 5672);
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		//使setConfirmCallback生效
		factory.setPublisherConfirms(true);
		//使setReturnCallback生效
		factory.setPublisherReturns(true);
		return factory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setEncoding("UTF-8");
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		//如果要使得setReturnCallback生效,mandatory必须为true,否则无法获取返回的消息
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setReturnCallback(new ReturnCallbackListener());
		rabbitTemplate.setConfirmCallback(new ConfirmCallbackListener());
		return rabbitTemplate;
	}

	@Bean
	public Queue queue() {
		return new Queue("CONFIRM_TEST", true);
	}

	@Bean
	public Exchange exchange() {
		return new DirectExchange("DIRECT_EX", true, false);
	}

	@Bean
	public Binding binding(Queue queue, Exchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("ROUTING_KEY").noargs();
	}
}
