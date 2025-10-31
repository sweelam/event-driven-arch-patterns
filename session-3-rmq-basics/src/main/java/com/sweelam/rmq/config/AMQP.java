package com.sweelam.rmq.config;

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import static java.util.Objects.nonNull;

public class AMQP {
	private Connection conn;
	private Channel channel;

    private ConnectionFactory setupConnection(String username, String password, int port) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        return connectionFactory;
    }

	public Connection connect(String username, String password, int port)
            throws IOException, TimeoutException {
        var connectionFactory = setupConnection(username, password, port);
        return conn = connectionFactory.newConnection();
	}

    public Channel createChannel() throws IOException {
		assert conn != null : "Connections is not initialized";
		return nonNull(channel) ? channel : conn.createChannel();
	}

	public void createInfra(ExchangeBuilder exchange, String queueName,
                               Map<String, Object> args, String RK) throws IOException {

        if (channel == null) {
			channel = createChannel();
		}

        channel.exchangeDeclare(
                exchange.getExchangeName(),
                exchange.getType(),
                null != exchange.isDurable(),
                null != exchange.isAutoDelete(),
                exchange.getGetArguments());

        channel.queueDeclare(queueName, true, false, false, args);
        channel.queueBind(queueName, exchange.getExchangeName(), RK);
	}

	public void enableDeliveryAck() throws IOException {
		var selectOk = channel.confirmSelect();
		if (selectOk == null) {
			throw new RuntimeException("Auto Ack is not enabled");
		}
	}

}
