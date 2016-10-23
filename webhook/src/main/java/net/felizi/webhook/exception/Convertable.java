package net.felizi.webhook.exception;

import java.util.Map;

public interface Convertable {
	public Map<String, Object> toMap();
}
