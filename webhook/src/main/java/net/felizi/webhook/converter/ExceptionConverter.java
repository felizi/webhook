package net.felizi.webhook.converter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.felizi.webhook.constants.ExceptionConstants;

@Component
public class ExceptionConverter {
	public Map<String, Object> execute(Exception e) {
		Map<String, Object> converted = new HashMap<>();
		converted.put(ExceptionConstants.MESSAGE, e.getMessage());
		return converted;
	}
}
