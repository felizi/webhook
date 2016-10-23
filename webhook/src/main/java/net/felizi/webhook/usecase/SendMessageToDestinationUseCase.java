package net.felizi.webhook.usecase;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.felizi.webhook.domain.Destination;
import net.felizi.webhook.domain.Message;
import net.felizi.webhook.rabbitmq.WebhookMessageProducer;
import net.felizi.webhook.repository.DestinationRepository;

@Component
public class SendMessageToDestinationUseCase {
	@Autowired
	private DestinationRepository destinationRepository;
	@Autowired
	private WebhookMessageProducer producer;

	public void execute(Message message) throws AmqpException {
		Destination destination = destinationRepository.findOne(message.getId());
		if (destination != null) {
			producer.send(message);
		}
	}
}