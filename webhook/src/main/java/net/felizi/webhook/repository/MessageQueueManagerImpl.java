package net.felizi.webhook.repository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.felizi.webhook.rabbitmq.WebhookMessageListener;

@Repository
@Singleton
public class MessageQueueManagerImpl implements MessageQueueManager {
	@Autowired
	private AmqpAdmin admin;
	@Autowired
	private AmqpTemplate template;
	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private DirectExchange exchange;
	@Autowired
	private JsonMessageConverter jsonMessageConverter;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DestinationRepository destinationRepository;

	private Map<String, SimpleMessageListenerContainer> queueContainer = new HashMap<>();

	@Override
	public synchronized String createQueue(String queueName) {
		if (!queueContainer.containsKey(queueName)) {
			boolean durable = true;
			boolean autoDelete = false;
			boolean exclusive = false;

			Queue newQueue = new Queue(queueName, durable, exclusive, autoDelete);

			queueName = admin.declareQueue(newQueue);

			admin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchange.getName(), queueName, null));

			SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
			container.addQueueNames(queueName);
			container.addQueues(newQueue);
			container.setMessageListener(new MessageListenerAdapter(new WebhookMessageListener(objectMapper, destinationRepository)));
			container.setConnectionFactory(connectionFactory);
			container.setAcknowledgeMode(AcknowledgeMode.AUTO);
			container.setMessageConverter(jsonMessageConverter);

			container.start();

			queueContainer.put(queueName, container);
		}
		return queueName;
	}

	@Override
	public void sendMessage(String destinationQueueName, Object message) throws AmqpException {
		template.convertAndSend(exchange.getName(), destinationQueueName, message);
	}
}