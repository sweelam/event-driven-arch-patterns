package com.kafka;

import com.kafka.producer.KProducer;

public class Main {
    public static void main(String[] args) {
        KProducer kProducer = new KProducer();
        kProducer.sendMessage();
    }
}