package com.kafka.avro.producer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import com.kafka.avro.config.KafkaStreamConfig;

public class KafkaStreamProducer {
    private final KafkaStreamConfig kafkaStreamConfig;
    private static final String ORDER_TAKER_TOPIC = "order-taker";

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

        KStream<String, String> stream = builder.stream(ORDER_TAKER_TOPIC);
        stream.foreach((i, t) -> System.out.println(i + " " + t));

        new KafkaStreams(builder.build(), kafkaStreamConfig.getProperties())
                .start();
    }

    public static void main(String[] args) throws InterruptedException {
        var o = new KafkaStreamProducer();
        o.produce();
    }
}


