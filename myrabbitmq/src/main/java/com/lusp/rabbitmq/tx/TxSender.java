package com.lusp.rabbitmq.tx;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class TxSender {
	
	public static final String QUEUE_NAME = "test_queue_tx";

	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		String msg = "hello tx message!";
		
		try {
			channel.txSelect();
			
			msg += 1/0;
			
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			
			System.out.println("---send tx message!");
			
			channel.txCommit();
			
		} catch (Exception e) {
			
			channel.txRollback();
			
			System.out.println("send message rollback!");
		}
		
		channel.close();
		
		connection.close();
		
	}

}
