package com.lusp.rabbitmq.confirm;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import com.lusp.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

public class Sender3 {

	public static final String QUEUE_NAME = "test_queue_confirm_synch";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		Connection connection = ConnectionUtils.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
		
		channel.confirmSelect();
		
		channel.addConfirmListener(new ConfirmListener() {
			
			//如果没有问题
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				if(multiple) {
					System.out.println("---handleAck---multiple true");
					confirmSet.headSet(deliveryTag + 1).clear();
				}else {
					System.out.println("---handleAck---multiple false");
					confirmSet.remove(deliveryTag);
				}
				
			}
			
			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				if(multiple) {
					System.out.println("---handleAck---multiple");
					confirmSet.headSet(deliveryTag + 1).clear();
				}else {
					System.out.println("---handleAck---multiple false");
					confirmSet.remove(deliveryTag);
				}
			}
		});
		
		String msg = "---send confirm sysnch message!";
		
		while(true){
			long seqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			confirmSet.add(seqNo);
		}
	}

}
