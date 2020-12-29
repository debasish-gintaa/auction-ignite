package com.asconsoft.gintaa.payload;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.multimap.Multimap;

import java.util.List;

public class FieldError {

	private String field;
	
	private List<String> messages = Lists.mutable.empty();

	public FieldError() {}

	private FieldError(String field) {
		super();
		this.field = field;
	}
	
	private FieldError(String field, List<String> messages) {
		super();
		this.field = field;
		this.messages = messages;
	}

	public static FieldError of(String field, List<String> messages) {
		return new FieldError(field, messages);
	}
	public static FieldError of(String field) {
		return new FieldError(field);
	}

	public String getField() {
		return field;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void addMessage(String message) {
		this.messages.add(message);
	}

	public static List<FieldError> from(Multimap<String, String> fieldErrors) {
		return fieldErrors.keyMultiValuePairsView()
				.collect(p -> FieldError.of(p.getOne(), p.getTwo().toList()))
				.toList();
	}
}