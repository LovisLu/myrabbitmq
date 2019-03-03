package com.lusp.rabbitmq.routing;

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
	
	public static final String QUEUE_NAME = "test_queue_direct_2";
	
	public static final String EXCHANGE_NAME = "test_exchange_direct";

	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.basicQos(1);
		
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "erro");
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "info");
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "warning");
		
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
