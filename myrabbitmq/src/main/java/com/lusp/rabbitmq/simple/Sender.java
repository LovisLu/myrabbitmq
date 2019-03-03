package com.lusp.rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Sender {
	
	public static final String QUEUE_NAME = "test_simple_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		String msg = "hello simple queue!";
		
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
		
		System.out.println("--send msg:" + msg);
		
		channel.close();
		
		connection.close();
	}
}
