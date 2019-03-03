package com.lusp.rabbitmq.pb;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 订阅模式
 * @author Administrator
 *
 */
public class Sender {
	
	public static final String EXCHANGE_NAME = "test_exchange_fanout";

	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		//声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
		
		String msg = "hello pb";
		
		channel.basicPublish(EXCHANGE_NAME, "", null,  msg.getBytes());
		
		System.out.println("---send msg:" + msg);
		
		channel.close();
		
		connection.close();

	}

}
