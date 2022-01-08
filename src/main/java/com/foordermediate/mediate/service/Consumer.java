package com.foordermediate.mediate.service;

import com.foordermediate.mediate.config.QueueConfig;
import com.foordermediate.mediate.constants.QueueType;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void consume(QueueType type) throws Exception {
        Integer time = LocalDateTime.now().getMinute();
        System.out.println(time);
        Channel channel = ChannelService.getChannel(type, time);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'" + properties.getPriority());
                if(message.equals("stop")){
                    try {
                        ChannelService.closeChannel(channel);
                        System.out.println("stopped");
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        channel.basicConsume(QueueConfig.getQueue(type, time), true, consumer);
    }
}