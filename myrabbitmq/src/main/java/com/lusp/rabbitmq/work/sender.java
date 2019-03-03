package com.lusp.rabbitmq.work;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
/**
 * 轮询分发（round-robin）
 * @author Administrator
 *
 */
public class sender {
	
	public static final String QUEUE_NAME = "test_work_queue";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		//获取连接
		Connection connection = ConnectionUtils.getConnection();
		//获取通道
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		for (int i = 0; i < 50; i++) {
			String msg = "---hello work queue " + i;
			System.out.println("[WQ] send:" + msg);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			
			Thread.sleep(i + 100);
			
		}
		
		channel.close();
		connection.close();

	}

}
