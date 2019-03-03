package com.lusp.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
/**
 * 
 * @author Administrator
 *
 */
public class Receiver {

	public static final String QUEUE_NAME = "test_queue_confirm";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println("receive [confirm] msg:" + new String(body,"UTF-8"));
			}
		});
	}
}
