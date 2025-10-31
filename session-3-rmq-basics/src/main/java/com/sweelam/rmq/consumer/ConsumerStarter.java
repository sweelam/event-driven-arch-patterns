package com.sweelam.rmq.consumer;

import static com.sweelam.rmq.config.AmqpConfig.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.sweelam.rmq.config.AMQP;

public class ConsumerStarter {
    private static final ExecutorService ex =
            Executors.newFixedThreadPool(5);

    private static Consumer consumer;

    public static void main(String[] args) throws IOException, TimeoutException {
		var amqp = new AMQP();
        consumer = new Consumer();

		amqp.connect(USERNAME, PASSWORD, 5672);
        var channel = amqp.createChannel();
        for (int i = 0; i < 5; i++) {
            ex.submit(() -> consumer.consumerMessage(channel));
        }
	}
}
