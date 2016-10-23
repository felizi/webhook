package net.felizi.webhook.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.felizi.webhook.constants.QueueConstants;
import net.felizi.webhook.domain.Message;
import net.felizi.webhook.repository.MessageQueueManagerImpl;

@Component
public class WebhookMessageProducer {
	@Autowired
	private MessageQueueManagerImpl messageQueueManager;

	public void send(Message message) throws AmqpException {
		String queueName = messageQueueManager.createQueue(QueueConstants.QUEUE_NAME + message.getId());
		messageQueueManager.sendMessage(queueName, message);
	}
}