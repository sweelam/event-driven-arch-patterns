package com.kafka.streams.producer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.AutoOffsetReset;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import com.kafka.streams.config.KafkaStreamConfig;

public class KafkaStreamProducer {
    private final KafkaStreamConfig kafkaStreamConfig;
    private static final String ORDER_TAKER_TOPIC = "order-handler-topic";

    public KafkaStreamProducer() {
        this.kafkaStreamConfig = new KafkaStreamConfig();
    }

    public void produce() {
        StreamsBuilder builder = new StreamsBuilder();

        try (KafkaProducer<String, String> kafkaProducer = 
                new KafkaProducer<>(kafkaStreamConfig.getProperties())) {

            var ints = IntStream.range(1, 10)
                    .boxed()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            kafkaProducer.send(new ProducerRecord<>(ORDER_TAKER_TOPIC, "numbers", ints));
            
        }

        KStream<String, String> stream = 
            builder.stream(ORDER_TAKER_TOPIC, Consumed.with(AutoOffsetReset.earliest()));
            
        stream.foreach((i, t) -> System.out.println(i + " " + t));

        new KafkaStreams(builder.build(), kafkaStreamConfig.getProperties())
                .start();
    }
}


