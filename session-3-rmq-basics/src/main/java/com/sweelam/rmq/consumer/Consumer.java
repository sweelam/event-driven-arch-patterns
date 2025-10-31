package com.sweelam.rmq.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.UUID;

import static com.sweelam.rmq.config.AmqpConfig.QUEUE_NAME;

public class Consumer {
    public void consumerMessage(Channel channel) {
        try {
            var tag = channel.basicConsume(QUEUE_NAME, false, "tagTest-" + UUID.randomUUID(),
                    new DefaultConsumer(channel) {

                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {

                            var mssg = new String(body);
                            System.out.printf("[Thread # %d] with mssg %s%n", Thread.currentThread().getId(), mssg);

                            if (envelope.isRedeliver()) {
                                System.out.println("Message " + envelope.getDeliveryTag() + " Delivered before");
                            }
                            channel.basicAck(envelope.getDeliveryTag(), true);
                        }

                    });

            System.out.println("============&&&&&&&&&&************================");
            System.out.println(tag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void consumerMessage_o(Channel channel) {
        try {
            var tag = channel.basicConsume(QUEUE_NAME, false, "tagTest-" + UUID.randomUUID(),
                    new DefaultConsumer(channel) {

                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                                   byte[] body) throws IOException {

                            var mssg = new String(body);
                            System.out.println(
                                    String.format("[Thread # %d] with mssg %s", Thread.currentThread().getId(), mssg));

                            if (envelope.isRedeliver()) {
                                System.out.println("Message " + envelope.getDeliveryTag() + " Delivered before");
                                System.exit(0);
                            }

                            var mssgArr = mssg.split(" ");
                            if ("600".equals(mssgArr[2])) {
                                System.out.println(String.format("Negative Ack for mssg %s with delivery tag %d", mssg,
                                        envelope.getDeliveryTag()));

                                var duration = (System.currentTimeMillis() - properties.getTimestamp().getTime())
                                        / 1_000;
                                System.out.println(String.format("Message took time %s", duration));

                                channel.basicNack(envelope.getDeliveryTag(), false, false);
                                System.exit(0);
                            } else {
                                channel.basicAck(envelope.getDeliveryTag(), true);
                            }

                            channel.basicAck(envelope.getDeliveryTag(), true);

                        }

                    });

            System.out.println("============&&&&&&&&&&************================");
            System.out.println(tag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
