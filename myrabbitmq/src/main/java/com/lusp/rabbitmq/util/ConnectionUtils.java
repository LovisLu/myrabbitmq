package com.lusp.rabbitmq.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtils {

	/**
	 * 获取mq连接
	 * @return
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public static Connection getConnection() throws IOException, TimeoutException {
		
		ConnectionFactory factory = new ConnectionFactory();
		
		factory.setHost("127.0.0.1");
		
		factory.setPort(5672);
		
		factory.setVirtualHost("/vhost_mymq");
		
		factory.setUsername("mymq");
		
		factory.setPassword("123456");
		
		return factory.newConnection();
	}
}
