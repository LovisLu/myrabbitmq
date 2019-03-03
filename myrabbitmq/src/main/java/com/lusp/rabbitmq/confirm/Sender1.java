package com.lusp.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * confirm串行模式（单条）
 * @author Administrator
 *
 */
public class Sender1 {
	
	public static final String QUEUE_NAME = "test_queue_confirm";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		String msg = "hello confirm message!";
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		
		channel.confirmSelect();
		
		if(!channel.waitForConfirms()) {
			System.out.println("message send failded");
		}else {
			System.out.println("message send ok");
		}
		
		channel.close();
		connection.close();

	}

}
