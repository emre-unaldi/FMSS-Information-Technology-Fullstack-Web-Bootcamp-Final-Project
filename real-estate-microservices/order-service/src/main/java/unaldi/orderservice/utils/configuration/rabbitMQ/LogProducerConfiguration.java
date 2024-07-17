package unaldi.orderservice.utils.configuration.rabbitMQ;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
@Configuration
@Data
public class LogProducerConfiguration {

    @Value("${rabbitmq.logs.queue}")
    private String queueName;

    @Value("${rabbitmq.logs.exchange}")
    private String exchange;

    @Value("${rabbitmq.logs.routingKey}")
    private String routingKey;

    @Bean(name = "logQueue")
    public Queue logQueue() {
        return new Queue(queueName, false);
    }

    @Bean(name = "logExchange")
    public DirectExchange logExchange() {
        return new DirectExchange(exchange);
    }

    @Bean(name = "logBinding")
    public Binding logBinding(@Qualifier("logQueue") Queue queue, @Qualifier("logExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}