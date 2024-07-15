package unaldi.logservice.utils.rabbitMQ.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unaldi.logservice.repository.LogRepository;
import unaldi.logservice.service.mapper.LogMapper;
import unaldi.logservice.utils.constants.Messages;
import unaldi.logservice.utils.rabbitMQ.dto.LogDTO;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Component
public class LogConsumer {

    private final Logger logger = LoggerFactory.getLogger( LogConsumer.class );
    private final LogRepository logRepository;

    @Autowired
    public LogConsumer(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @RabbitListener(queues = "${rabbitmq.logs.queue}")
    public void fetchLogAndSaveToMongoDB(LogDTO logDTO) {
        logger.info(Messages.LOG_READ_QUEUE + " : {}", logDTO);

        logRepository.save(LogMapper.INSTANCE.logDtoToLog(logDTO));
    }

}
