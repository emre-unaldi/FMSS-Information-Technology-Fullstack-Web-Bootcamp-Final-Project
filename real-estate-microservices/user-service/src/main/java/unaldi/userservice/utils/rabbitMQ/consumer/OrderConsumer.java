package unaldi.userservice.utils.rabbitMQ.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import unaldi.userservice.service.AccountService;
import unaldi.userservice.utils.rabbitMQ.dto.OrderDTO;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Component
public class OrderConsumer {

    private final Logger logger = LoggerFactory.getLogger( OrderConsumer.class );
    private final AccountService accountService;

    public OrderConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @RabbitListener(queues = "${rabbitmq.orders.queue}")
    public void fetchOrder(OrderDTO orderDTO) {
        logger.info("Order record read from queue : {}", orderDTO);

        try {
            accountService.updateWithOrder(orderDTO);
        } catch (Exception exception) {
            logger.error("Error information updating account with order", exception);
            throw new RuntimeException("Failed to update account information with order", exception);
        }
    }

}
