package com.lusp.rabbitmq.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Sender {
	
	public static final String EXCHANGE_NAME = "test_exchange_topic";

	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		
		String msg = "商品。。。";
		
		channel.basicPublish(EXCHANGE_NAME, "goods.insert", null, msg.getBytes());
		
		System.out.println("---send msg:" + msg);
		
		channel.close();
		
		connection.close();

	}

}
