package net.felizi.webhook.domain;

import java.util.Map;

import net.felizi.webhook.exception.InvalidArgumentException;
import net.felizi.webhook.exception.MultipleException;

public class Message {
	private Long id;
	private String contentType;
	private Map<String, Object> body;

	public Message() {
		super();
	}

	public Message(Long id, String contentType, Map<String, Object> body) throws MultipleException {
		MultipleException multipleException = new MultipleException();
		if (id == null || id.longValue() <= 0) {
			multipleException.addException(new InvalidArgumentException("ID", "This value is required and great than zero"));
		}
		if (contentType == null || contentType.trim().isEmpty()) {
			multipleException.addException(new InvalidArgumentException("CONTENT_TYPE", "This value is required and not empty"));
		}
		if (body == null) {
			multipleException.addException(new InvalidArgumentException("BODY", "This value is required"));
		}

		if (multipleException.hasExceptions()) {
			throw multipleException;
		}

		this.id = id;
		this.contentType = contentType;
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public String getContentType() {
		return contentType;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
}