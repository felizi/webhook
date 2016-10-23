package net.felizi.webhook.rabbitmq;

import java.io.IOException;
import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import net.felizi.webhook.domain.Destination;
import net.felizi.webhook.repository.DestinationRepository;

@Component
public class WebhookMessageListener implements ChannelAwareMessageListener {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private DestinationRepository destinationRepository;

	@Override
	public void onMessage(Message message, Channel channel) {
		try {
			net.felizi.webhook.domain.Message webhookMsg = objectMapper.readValue(new String(message.getBody()), net.felizi.webhook.domain.Message.class);

			Destination destination = destinationRepository.findOne(webhookMsg.getId());

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.valueOf(webhookMsg.getContentType()));

			HttpEntity<Map> entity = new HttpEntity<Map>(webhookMsg.getBody(), headers);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
				protected boolean hasError(HttpStatus statusCode) {
					return false;
				}
			});
			// send request and parse result
			try {
				ResponseEntity<String> response = restTemplate.exchange(destination.getUrl(), HttpMethod.POST, entity, String.class);
				if (response.getStatusCode() == org.springframework.http.HttpStatus.OK) {
					System.out.println(response.getBody());
				} else {
					channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
					System.out.println("Fail: " + response.getStatusCode());
					System.out.println("Response: " + response);
					System.out.println("Body: " + response.getBody());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}