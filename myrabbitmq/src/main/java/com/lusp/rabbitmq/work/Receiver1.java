package com.lusp.rabbitmq.work;

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
 * 轮询分发（round-robin）,结果是不管谁忙谁清闲，都不会多给一个消息，任务消息总是均分的
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
				}
			}
		};
		//消费的确认模式：自动应答,一旦rabbitmq将消息发送给消费者，将从内存中删除，这种情况下，如果杀死正在执行的消费者就将丢失正在处理的消息
		boolean autoAck = true;
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
		
		
		
	}
}
