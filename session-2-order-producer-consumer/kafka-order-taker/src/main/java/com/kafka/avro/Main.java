package com.kafka.avro;

import com.kafka.avro.producer.KProducer;

public class Main {
    public static void main(String[] args) {
        KProducer kProducer = new KProducer();
        kProducer.sendMessage();
    }
}