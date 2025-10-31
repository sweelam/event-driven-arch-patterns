package com.sweelam.rmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.sweelam.rmq.config.AMQP;
import com.sweelam.rmq.config.ExchangeBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static com.sweelam.rmq.config.AmqpConfig.*;
import static com.sweelam.rmq.config.AmqpConfig.PASSWORD;
import static com.sweelam.rmq.config.AmqpConfig.QUEUE_NAME;

public class Producer {
    public static final String CONTENT_TYPE = "text/plain";
    private final AMQP amqp;
    private int messageCount = 0;

    public Producer() {
        amqp = new AMQP();
    }

    public void sendMessage() throws IOException, InterruptedException, TimeoutException {
        setupConnection();
        var mainExchange = buildExchange();

        amqp.createInfra(mainExchange, QUEUE_NAME, null, ROUTING_K);
        amqp.enableDeliveryAck();

        var channel = amqp.createChannel();
        for (int i = 0; i < 1000; i++) {
            publish(channel, i);
        }

        channel.waitForConfirmsOrDie(50L);
        System.out.println(messageCount + " messages were published");
    }

    private void setupConnection() {
        try {
            amqp.connect(USERNAME, PASSWORD, 5672);
        } catch (Exception e) {
            System.err.println("Error while connecting to RabbitMQ" + e);
        }
    }

    private ExchangeBuilder buildExchange() {
        return ExchangeBuilder.builder()
                .exchangeName(EXCHANGE)
                .type(BuiltinExchangeType.DIRECT.name().toLowerCase());
    }

    private void publish(Channel channel, int i) throws IOException {
        byte[] message = ("Test Message# " + i + " from Sweelam").getBytes();
        channel.basicPublish(
                EXCHANGE, ROUTING_K,
                new com.rabbitmq.client.AMQP.BasicProperties()
                        .builder()
                        .deliveryMode(2)
                        .contentType(CONTENT_TYPE)
                        .timestamp(Date.from(Instant.now()))
                        .build(),
                message
        );
        messageCount++;
    }
}
