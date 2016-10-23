package net.felizi.webhook.repository;

import org.springframework.amqp.AmqpException;

public interface MessageQueueManager {
	public String createQueue(String queueName);

	public void sendMessage(String destinationQueueName, Object message) throws AmqpException;
}