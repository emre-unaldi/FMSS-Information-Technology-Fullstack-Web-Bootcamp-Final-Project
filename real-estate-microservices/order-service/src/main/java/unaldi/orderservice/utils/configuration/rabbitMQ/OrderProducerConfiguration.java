package unaldi.orderservice.utils.configuration.rabbitMQ;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Data
@Configuration
public class OrderProducerConfiguration {

    @Value("${rabbitmq.orders.queue}")
    private String queueName;

    @Value("${rabbitmq.orders.exchange}")
    private String exchange;

    @Value("${rabbitmq.orders.routingKey}")
    private String routingKey;

    @Bean(name = "orderQueue")
    public Queue orderQueue() {
        return new Queue(queueName, false);
    }

    @Bean(name = "orderExchange")
    public DirectExchange orderExchange() {
        return new DirectExchange(exchange);
    }

    @Bean(name = "orderBinding")
    public Binding orderBinding(@Qualifier("orderQueue") Queue queue, @Qualifier("orderExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

}
