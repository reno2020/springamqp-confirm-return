package org.throwable.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.throwable.Application;
import org.throwable.entity.MessageTarget;

import static org.junit.Assert.*;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/6/22 1:06
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class PublishServiceTest {

	@Autowired
	private PublishService publishService;

	/**
	 * 注意Callback都是异步的,要sleep一下等待返回
	 * 正确的Exchange名为:DIRECT_EX,并且是DirectExchange
	 * 正确的ROUTING_KEY为:ROUTING_KEY
	 * 下面分九种情况验证所有组合
	 */

	@Test
	public void send1() throws Exception {
		String message = System.currentTimeMillis() + "$$send1";
		MessageTarget target = new MessageTarget("send1", message);
		publishService.send("DIRECT_EX", "ROUTING_KEY", target);
		Thread.sleep(1000);
		//exchange、routingKey都正确
		//只有ConfirmCallbackListener被回调
		//ConfirmCallbackListener回调结果:ConfirmCallbackListener>>confirm--:correlationData:CorrelationData [id=send1],ack:true,cause:null
		//分析:confirm被回调,ack=true
	}

	@Test
	public void send2() throws Exception {
		String message = System.currentTimeMillis() + "$$send2";
		MessageTarget target = new MessageTarget("send2", message);
		publishService.send("DIRECT_EX", "ROUTING_KEY_ERROR", target);
		Thread.sleep(1000);
		//exchange正确,routingKey错误
		//ConfirmCallbackListener、ReturnCallbackListener都被回调
		//ConfirmCallbackListener回调结果:ConfirmCallbackListener>>confirm--:correlationData:CorrelationData [id=send2],ack:true,cause:null
		//ReturnCallbackListener:ReturnCallbackListener>>return--message:{"id":"send2","body":"1498066015931$$send2"},replyCode:312,replyText:NO_ROUTE,exchange:DIRECT_EX,routingKey:ROUTING_KEY_ERROR
		//分析:confirm被回调,ack=true;return被回调,返回发送具体内容以及原因replyText:NO_ROUTE(路由失败)
	}

	@Test
	public void send3() throws Exception {
		String message = System.currentTimeMillis() + "$$send3";
		MessageTarget target = new MessageTarget("send3", message);
		publishService.send("DIRECT_EX_ERROR", "ROUTING_KEY", target);
		Thread.sleep(1000);
		//exchange错误,routingKey正确
		//只有ConfirmCallbackListener被回调
		//ConfirmCallbackListener回调结果:ConfirmCallbackListener>>confirm--:correlationData:CorrelationData [id=send3],ack:false,cause:channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'DIRECT_EX_ERROR' in vhost '/', class-id=60, method-id=40)
		//分析:confirm被回调,ack=false,原因描述=no exchange 'DIRECT_EX_ERROR' in vhost '/'
		//抛出了异常:Channel shutdown: channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'DIRECT_EX_ERROR' in vhost '/', class-id=60, method-id=40)
	}

	@Test
	public void send4() throws Exception {
		String message = System.currentTimeMillis() + "$$send4";
		MessageTarget target = new MessageTarget("send4", message);
		publishService.send("DIRECT_EX_ERROR", "ROUTING_KEY_ERROR", target);
		Thread.sleep(1000);
		//exchange、routingKey都错误
		//只有ConfirmCallbackListener被回调
		//ConfirmCallbackListener回调结果:ConfirmCallbackListener>>confirm--:correlationData:CorrelationData [id=send4],ack:false,cause:channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'DIRECT_EX_ERROR' in vhost '/', class-id=60, method-id=40)
		//分析:confirm被回调,ack=false,原因描述=no exchange 'DIRECT_EX_ERROR' in vhost '/'
		//抛出了异常:Channel shutdown: channel error; protocol method: #method<channel.close>(reply-code=404, reply-text=NOT_FOUND - no exchange 'DIRECT_EX_ERROR' in vhost '/', class-id=60, method-id=40)
	}


}