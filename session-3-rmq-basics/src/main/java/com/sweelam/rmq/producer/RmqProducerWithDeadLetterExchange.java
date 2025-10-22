package com.sweelam.rmq.producer;

import static com.sweelam.rmq.config.AmqpConfig.EXCHANGE;
import static com.sweelam.rmq.config.AmqpConfig.QUEUE_NAME;
import static com.sweelam.rmq.config.AmqpConfig.ROUTING_K;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.sweelam.rmq.config.AMQP;
import com.sweelam.rmq.config.ExchangeBuilder;
import static com.sweelam.rmq.config.AmqpConfig.*;


public class RmqProducerWithDeadLetterExchange {
	private static AMQP amqp = new AMQP();

	public static void main(String[] args) throws IOException, TimeoutException {

		var queueName = QUEUE_NAME + "dlx";
		var exchangeName = EXCHANGE + "dlx";
		var DEAD_LETTER_EXCHANGE = EXCHANGE + "_dead_letter";

		var deadLetterExchangeBuilder = ExchangeBuilder.builder()
				.exchangeName(DEAD_LETTER_EXCHANGE)
				.type(BuiltinExchangeType.FANOUT.name().toLowerCase());

		Map<String, Object> exchangeArgs = Map.of(
				"x-dead-letter-exchange", DEAD_LETTER_EXCHANGE,
				"x-message-ttl", 60000);

		amqp.connect(USERNAME, PASSWORD, 5672);

		amqp.createInfra(
				deadLetterExchangeBuilder,
				queueName,
				exchangeArgs,
				ROUTING_K);

		var mainExchange = ExchangeBuilder.builder()
				.exchangeName(exchangeName)
				.type(BuiltinExchangeType.DIRECT.name().toLowerCase());

		amqp.createInfra(mainExchange, queueName,
				Map.of("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE), ROUTING_K);

		amqp.createInfra(mainExchange, queueName, null, ROUTING_K);
		amqp.getChannel().queueBind(queueName, DEAD_LETTER_EXCHANGE, ROUTING_K);
	}
}
