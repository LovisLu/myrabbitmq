package com.lusp.rabbitmq.pb;

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
	
	public static final String QUEUE_NAME = "test_queue_fanout_sms";
	public static final String EXCHANGE_NAME = "test_exchange_fanout";
 	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
		
		channel.basicQos(1);
		
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body,"UTF-8");
				System.out.println("[2] receive msg:" + msg);
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println("[2] done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		
		boolean autoAck = false;
		
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}
}
