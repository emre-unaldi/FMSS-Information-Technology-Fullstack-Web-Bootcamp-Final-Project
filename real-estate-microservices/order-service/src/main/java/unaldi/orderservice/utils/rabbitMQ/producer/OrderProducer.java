package unaldi.orderservice.utils.rabbitMQ.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unaldi.orderservice.utils.configuration.rabbitMQ.OrderProducerConfiguration;
import unaldi.orderservice.utils.constants.Messages;
import unaldi.orderservice.utils.rabbitMQ.dto.OrderDTO;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */

@Component
public class OrderProducer {

    private final Logger logger = LoggerFactory.getLogger( LogProducer.class );
    private final OrderProducerConfiguration orderProducerConfiguration;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public OrderProducer(OrderProducerConfiguration orderProducerConfiguration, AmqpTemplate amqpTemplate) {
        this.orderProducerConfiguration = orderProducerConfiguration;
        this.amqpTemplate = amqpTemplate;
    }

    public void sendToOrder(OrderDTO orderDTO) {
        logger.info(Messages.ORDER_WRITE_QUEUE + " : {}", orderDTO);

        amqpTemplate.convertAndSend(
                orderProducerConfiguration.getExchange(),
                orderProducerConfiguration.getRoutingKey(),
                orderDTO
        );
    }

}