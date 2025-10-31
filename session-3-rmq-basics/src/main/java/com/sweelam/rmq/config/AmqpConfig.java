package com.sweelam.rmq.config;

public class AmqpConfig {
	private AmqpConfig() {}

    // For demonstration purposes only, Don't do this in production.
    public static final String PASSWORD = "mypassword";
	public static final String USERNAME = "myuser";

	public static final String ROUTING_K = "put-RK";
	public static final String QUEUE_NAME = "pure-test-Q";
	public static final String EXCHANGE = "newTestExchange";
}
