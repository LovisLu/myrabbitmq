package com.lusp.rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * 获取MQ消息
 * @author Administrator
 *
 */
public class Receiver {
	
	public static final String QUEUE_NAME = "test_simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		
		//队列声明
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		
		channel.basicConsume(QUEUE_NAME, true, new DefaultConsumer(channel) {
			 /**
		     * 成功监听走这个
		     * @param consumerTag
		     */
		    public void handleConsumeOk(String consumerTag) {
		        System.out.println("handleConsumeOk..."+consumerTag);
		    }

		    public void handleCancelOk(String consumerTag) {
		        System.out.println("handleCancelOk..."+consumerTag);
		    }

		    public void handleCancel(String consumerTag) throws IOException {
		        System.out.println("handleCancel..."+consumerTag);
		    }

		    /**
		     * 发生异常走这个
		     * @param consumerTag
		     * @param sig 异常信息
		     */
		    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		        System.out.println("handleShutdownSignal..."+consumerTag);
		        System.out.println("handleShutdownSignal...Exception: "+sig.getMessage());

		    }

		    public void handleRecoverOk(String consumerTag) {
		        System.out.println("handleRecoverOk..."+consumerTag);
		    }

		    /**
		     * 消息接收处理走这个
		     * @param consumerTag
		     * @param envelope
		     * @param properties
		     * @param body
		     * @throws IOException
		     */
		    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		        System.out.println("handleDelivery..."+consumerTag);
		        String msg = new String(body);
		        System.out.println("msg: "+msg);
		    }
		});
		
//		channel.close();
		
//		connection.close();
	}

}
