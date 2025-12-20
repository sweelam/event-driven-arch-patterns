package com.kafka.streams.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;


public class KafkaStreamConfig {
    private final Properties properties;
    
    public KafkaStreamConfig() {
        this.properties = new Properties();
        this.properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-taker-stream");
        this.properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        this.properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        this.properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        this.properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.properties.put("auto.offset.reset", "earliest"); 
    }

    public Properties getProperties() {
        return this.properties;
    }
}
