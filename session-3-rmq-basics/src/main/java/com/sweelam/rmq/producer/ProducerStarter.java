package com.sweelam.rmq.producer;

public class ProducerStarter {
    public static void main(String[] args) throws Exception {
		var producer = new Producer();
        producer.sendMessage();
		System.exit(0);
	}
}
