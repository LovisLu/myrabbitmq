package com.lusp.rabbitmq.workfair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
/**
 * 公平分发（fair dipatch）
 * @author Administrator
 *
 */
public class Receiver1 {
	
	public static final String QUEUE_NAME = "test_work_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//获取通道
		Channel channel = connection.createChannel();
		//声明队列，主要防止消息接受者先运行此程序，队列还不存在时创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		int prefetchCount = 1;
		channel.basicQos(prefetchCount);
		
		//定义一个消费者
		Consumer consumer = new DefaultConsumer(channel) {
			//消息到达触发这个方法
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				
				System.out.println("[1] receive msg:" + msg);
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("[1] done ");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		//消费的确认模式：自动应答false，如果一个消费者挂掉，则会交付给另外的消费者
		//rabbitmq支持消息应答，消费者发送一个消息应答，告诉rabbitmq这个消息我已经处理完成，你可以删除了，然后rabbitmq就删除内存中的消息
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		
		
		
	}
}
