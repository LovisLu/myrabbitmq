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

public class Receiver2 {
	
	public static final String QUEUE_NAME = "test_work_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//获取通道
		Channel channel = connection.createChannel();
		//声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.basicQos(1);
		//定义一个消费者
		Consumer consumer = new DefaultConsumer(channel) {
			//消息到达触发这个方法
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"utf-8");
				
				System.out.println("[2] receive msg:" + msg);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("[2] done ");
					channel.basicAck(envelope.getDeliveryTag(),false);
				}
			}
		};
		boolean autoAck = false;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		
		
		
	}
}
