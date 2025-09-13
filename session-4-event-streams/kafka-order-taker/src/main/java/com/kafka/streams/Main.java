package com.kafka.streams;

import com.kafka.streams.producer.KafkaStreamProducer;

public class Main {
    public static void main(String[] args) {
        new KafkaStreamProducer().produce();
    }
}