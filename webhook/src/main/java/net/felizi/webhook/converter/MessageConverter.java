package net.felizi.webhook.converter;

import java.util.Map;

import org.springframework.stereotype.Component;

import net.felizi.webhook.constants.MessageConstants;
import net.felizi.webhook.domain.Message;
import net.felizi.webhook.exception.MultipleException;

@Component
public class MessageConverter {
	public Message execute(Long id, Map<String, Object> body) throws MultipleException {
		return new Message(id, extractContentType(body), extractBody(body));
	}

	private String extractContentType(Map<String, Object> body) {
		if (body != null && !body.isEmpty() && body.containsKey(MessageConstants.CONTENT_TYPE)) {
			return (String) body.get(MessageConstants.CONTENT_TYPE);
		}
		return null;
	}

	private Map<String, Object> extractBody(Map<String, Object> body) {
		if (body != null && !body.isEmpty() && body.containsKey(MessageConstants.BODY)) {
			return (Map<String, Object>) body.get(MessageConstants.BODY);
		}
		return null;
	}
}