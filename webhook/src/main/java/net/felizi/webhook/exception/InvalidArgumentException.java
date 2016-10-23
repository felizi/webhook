package net.felizi.webhook.exception;

import java.util.Map;

import net.felizi.webhook.converter.ExceptionConverter;

public class InvalidArgumentException extends Exception implements Convertable {
	private static final long serialVersionUID = 1L;
	private ExceptionConverter exceptionConverter = new ExceptionConverter();

	private String identifier;

	public InvalidArgumentException(String identifier, String message) {
		super(message);
		this.identifier = identifier;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public Map<String, Object> toMap() {
		Map<String, Object> converted = exceptionConverter.execute(this);
		converted.put("identifier", this.identifier);
		return converted;
	}
}