package com.sweelam.rmq.config;

import java.util.Map;


public class ExchangeBuilder {
	private String exchangeName;
	private String type;
	private Boolean passive;
	private Boolean durable;
	private Boolean autoDelete;
	private Map<String, Object> arguments;
	public static ExchangeBuilder builder() {
		return new ExchangeBuilder();
	}

	public ExchangeBuilder exchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
		return this;
	}

	public ExchangeBuilder type(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}

	public Boolean isDurable() {
		return durable;
	}

	public Boolean isAutoDelete() {
		return autoDelete;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public Map<String, Object> getGetArguments() {
		return arguments;
	}
}
