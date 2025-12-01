package com.anna.power.desafio_java_backend.infrastructure.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "vehicle.queue";
    public static final String EXCHANGE = "vehicle.exchange";
    public static final String ROUTING_KEY = "vehicle.created";

    @Bean
    public Queue vehicleQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public DirectExchange vehicleExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding vehicleBinding(Queue vehicleQueue, DirectExchange vehicleExchange) {
        return BindingBuilder
                .bind(vehicleQueue)
                .to(vehicleExchange)
                .with(ROUTING_KEY);
    }
}
