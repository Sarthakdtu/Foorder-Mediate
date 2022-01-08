package com.foordermediate.mediate.service;

import com.foordermediate.mediate.config.QueueConfig;
import com.foordermediate.mediate.constants.QueueType;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeoutException;

public class Publisher {

    public static void publish(QueueType type, String message) throws IOException, TimeoutException {
        Integer time = LocalDateTime.now().getMinute();
        System.out.println(time);
        String queue = QueueConfig.getQueue(type, time);
        Channel channel = ChannelService.getChannel(type, time);
        channel.basicPublish("", QueueConfig.getQueue(type, time),
                QueueConfig.queueProperty(time),
                message.getBytes(StandardCharsets.UTF_8)
        );
        ChannelService.closeChannel(channel);
    }
}
