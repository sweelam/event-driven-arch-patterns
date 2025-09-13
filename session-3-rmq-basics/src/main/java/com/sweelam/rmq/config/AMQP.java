package com.sweelam.rmq.config;

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class AMQP {
	private Connection conn;
	private Channel channel;

	public Connection connect(String username, String password, int port) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);

		conn = connectionFactory.newConnection();
		return conn;
	}

	public Channel getChannel() throws IOException, TimeoutException {
		assert conn != null : "Connections is not initialized";

		if (channel != null) {
			return channel;
		}

		return channel = conn.createChannel();
	}

	public boolean createInfra(String exchangeName, String exchangeType, String queueName,
			String RK) throws IOException, TimeoutException {
		
		if (channel == null) {
			getChannel();
		}

		try {
			channel.exchangeDeclare(exchangeName, exchangeType);
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchangeName, RK);
		} catch (IOException e) {
			return false;
		}

		return false;
	}

	public boolean createInfra(ExchangeBuilder exchange, String queueName, Map<String, Object> args, String RK)
			throws IOException, TimeoutException {
		if (channel == null) {
			getChannel();
		}

		try {

			channel.exchangeDeclare(
					exchange.getExchangeName(),
					exchange.getType(),
					null != exchange.isDurable(),
					null != exchange.isAutoDelete(),
					exchange.getGetArguments());

			channel.queueDeclare(queueName, true, false, false, args);
			channel.queueBind(queueName, exchange.getExchangeName(), RK);

		} catch (IOException e) {
			return false;
		}

		return false;
	}

	public void enableDeliveryAck() throws IOException, TimeoutException {
		if (channel == null) {
			getChannel();
		}

		var selectOk = channel.confirmSelect();
		if (selectOk == null) {
			throw new RuntimeException("Auto Ack is not enabled");
		}
	}

}
