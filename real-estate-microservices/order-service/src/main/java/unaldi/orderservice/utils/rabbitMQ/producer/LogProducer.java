package unaldi.orderservice.utils.rabbitMQ.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unaldi.orderservice.utils.configuration.rabbitMQ.RabbitMQProducerConfiguration;
import unaldi.orderservice.utils.constants.Messages;
import unaldi.orderservice.utils.rabbitMQ.dto.LogDTO;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Component
public class LogProducer {

    private final Logger logger = LoggerFactory.getLogger( LogProducer.class );
    private final RabbitMQProducerConfiguration rabbitMQProducerConfiguration;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public LogProducer(RabbitMQProducerConfiguration rabbitMQProducerConfiguration, AmqpTemplate amqpTemplate) {
        this.rabbitMQProducerConfiguration = rabbitMQProducerConfiguration;
        this.amqpTemplate = amqpTemplate;
    }

    public void sendToLog(LogDTO logDTO) {
        logger.info(Messages.LOG_WRITE_QUEUE + " : {}", logDTO);

        amqpTemplate.convertAndSend(
                rabbitMQProducerConfiguration.getExchange(),
                rabbitMQProducerConfiguration.getRoutingKey(),
                logDTO
        );
    }

}