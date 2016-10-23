package net.felizi.webhook.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.felizi.webhook.constants.ExceptionConstants;
import net.felizi.webhook.converter.ExceptionConverter;

public class MultipleException extends Exception implements Convertable {
	private static final long serialVersionUID = 1L;
	private ExceptionConverter exceptionConverter = new ExceptionConverter();

	private List<Exception> exceptions = new ArrayList<>();

	public MultipleException() {
		super();
	}

	public MultipleException(List<Exception> exceptions) {
		super();
		this.exceptions = exceptions;
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}

	public void addExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}

	public void addException(Exception exception) {
		this.exceptions.add(exception);
	}

	public boolean hasExceptions() {
		return !this.exceptions.isEmpty();
	}

	@Override
	public Map<String, Object> toMap() {
		List<Map<String, Object>> errorsList = new ArrayList<>();
		for (Exception exception : this.exceptions) {
			if (exception instanceof Convertable) {
				errorsList.add(((Convertable) exception).toMap());
			} else {
				errorsList.add(exceptionConverter.execute(exception));
			}
		}

		Map<String, Object> errors = new HashMap<>();
		errors.put(ExceptionConstants.ERRORS, errorsList);
		return errors;
	}
}