package com.lusp.rabbitmq.routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 路由模式（routing）
 * @author Administrator
 *
 */
public class Sender {
	
	public static final String EXCHANGE_NAME = "test_exchange_direct";

	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");
		
		String msg = "hello direct!";
		
		String routingKey = "erro";
		
		channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
		
		System.out.println("---send msg:" + msg);
		
		channel.close();
		
		connection.close();

	}

}
