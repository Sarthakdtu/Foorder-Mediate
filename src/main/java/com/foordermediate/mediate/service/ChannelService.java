package com.foordermediate.mediate.service;

import com.foordermediate.mediate.config.QueueConfig;
import com.foordermediate.mediate.config.RabbitMQConfig;
import com.foordermediate.mediate.constants.QueueType;
import com.rabbitmq.client.Channel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public abstract class ChannelService {

    public static Channel getChannel(QueueType type, Integer time) throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(RabbitMQConfig.class);
        Channel channel = context.getBean(Channel.class);
        HashMap<String, Object> queueParams = QueueConfig.queueParams(type, time);
        HashMap<String, Object>  args = (HashMap<String, Object>) queueParams.get("args");
        boolean durable = (boolean) queueParams.getOrDefault("durable", false);
        boolean exclusive = (boolean) queueParams.getOrDefault("exclusive", false);
        boolean autoDelete = (boolean) queueParams.getOrDefault("autoDelete", false);
        String queue = (String) queueParams.get("queue");

        channel.queueDeclare(queue, durable, exclusive, autoDelete, args);
        return channel;
    }

    public static void closeChannel(Channel channel) throws IOException, TimeoutException {
        channel.close();
        channel.getConnection().close();
        System.out.println("Connection closed");
    }
}
