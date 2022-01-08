package com.foordermediate.mediate.config;

import com.foordermediate.mediate.constants.QueueType;
import com.rabbitmq.client.AMQP;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public abstract class QueueConfig {

    static Logger logger = LoggerFactory.getLogger(QueueConfig.class.getName());

    private static final Integer MAX_PRIORITY = 65;

    private static final String oddPickup = "oddPickup";

    private static final String evenPickup = "evenPickup";

    private static final String oddDelivery = "oddDelivery";

    private static final String evenDelivery = "evenDelivery";

    @Getter private static final String healthQueue = "health";

    public static String getPickupQueue(Integer time){
        if(time % 2 == 0){
            return evenPickup;
        }
        else{
            return oddPickup;
        }
    }

    public static String getDeliveryQueue(Integer time){
        if(time % 2 == 0){
            return evenDelivery;
        }
        else{
            return oddDelivery;
        }
    }

    public static String getQueue(QueueType type, Integer time){
        String queue;
        if(type == QueueType.PICKUP){
           queue = getPickupQueue(time);
        }
        else if(type == QueueType.DELIVERY){
            queue = getDeliveryQueue(time);
        }
        else {
            queue = getHealthQueue();
        }
        logger.info("Using Queue : " + queue);
        return queue;
    }

    public static HashMap<String, Object> queueParams(QueueType type, Integer time){
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-max-priority", MAX_PRIORITY);
        boolean durable = true;    //durable - RabbitMQ will never lose the queue if a crash occurs
        boolean exclusive = false;  //exclusive - if queue only will be used by one connection
        boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes
        String queue = getQueue(type, time);
        params.put("queue", queue);
        params.put("durable", durable);
        params.put("exclusive", exclusive);
        params.put("autoDelete", autoDelete);
        params.put("args", args);
        return params;
    }

    public static AMQP.BasicProperties queueProperty(Integer time){
        return new AMQP.BasicProperties.Builder().contentType("text/plain").priority(time).build();
    }
}
