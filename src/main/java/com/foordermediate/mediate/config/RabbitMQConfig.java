package com.foordermediate.mediate.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.foordermediate.*" })
@PropertySource("classpath:config.properties")
public class RabbitMQConfig {

    @Value("${uri}")
    private String uri;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setUri(uri);
        cachingConnectionFactory.setConnectionTimeout(30000);
        return cachingConnectionFactory;
    }

    @Bean
    public Connection getConnection() throws Exception {
        return connectionFactory().getRabbitConnectionFactory().newConnection();
    }


    @Bean
    public Channel getChannel() throws Exception {
//        boolean durable = false;    //durable - RabbitMQ will never lose the queue if a crash occurs
//        boolean exclusive = false;  //exclusive - if queue only will be used by one connection
//        boolean autoDelete = false; //autodelete - queue is deleted when last consumer unsubscribes
        //        Map<String, Object> arg = new HashMap<String, Object>();
//        arg.put("x-max-priority", 65);

        return getConnection().createChannel();
    }


}