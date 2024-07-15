package unaldi.logservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unaldi.logservice.entity.Log;
import unaldi.logservice.entity.dto.request.LogSaveRequest;
import unaldi.logservice.entity.dto.request.LogUpdateRequest;
import unaldi.logservice.entity.dto.response.LogResponse;
import unaldi.logservice.repository.LogRepository;
import unaldi.logservice.service.LogService;
import unaldi.logservice.service.mapper.LogMapper;
import unaldi.logservice.utils.constants.ExceptionMessages;
import unaldi.logservice.utils.constants.Messages;
import unaldi.logservice.utils.exception.LogNotFoundException;
import unaldi.logservice.utils.result.DataResult;
import unaldi.logservice.utils.result.Result;
import unaldi.logservice.utils.result.SuccessDataResult;
import unaldi.logservice.utils.result.SuccessResult;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public DataResult<LogResponse> save(LogSaveRequest logSaveRequest) {
        Log log = LogMapper.INSTANCE.logSaveRequestToLog(logSaveRequest);

        logRepository.save(log);

        return new SuccessDataResult<>(
                LogMapper.INSTANCE.logToLogResponse(log),
                Messages.LOG_SAVED
        );
    }

    @Override
    public DataResult<LogResponse> update(LogUpdateRequest logUpdateRequest) {
        if (!this.logRepository.existsById(logUpdateRequest.getId())) {
            throw new LogNotFoundException(ExceptionMessages.LOG_NOT_FOUND);
        }

        Log log = LogMapper.INSTANCE.logUpdateRequestToLog(logUpdateRequest);
        logRepository.save(log);

        return new SuccessDataResult<>(
                LogMapper.INSTANCE.logToLogResponse(log),
                Messages.LOG_UPDATED
        );
    }

    @Override
    public Result deleteById(String logId) {
        Log log = logRepository
                .findById(logId)
                .orElseThrow(() -> new LogNotFoundException(ExceptionMessages.LOG_NOT_FOUND));

        logRepository.deleteById(log.getId());

        return new SuccessResult(Messages.LOG_DELETED);
    }

    @Override
    public DataResult<LogResponse> findById(String logId) {
        LogResponse logDTO = this.logRepository
                .findById(logId)
                .map(LogMapper.INSTANCE::logToLogResponse)
                .orElseThrow(() -> new LogNotFoundException(ExceptionMessages.LOG_NOT_FOUND));

        return new SuccessDataResult<>(logDTO, Messages.LOG_FOUND);
    }

    @Override
    public DataResult<List<LogResponse>> findAll() {
        List<Log> logs = this.logRepository.findAll();

        return new SuccessDataResult<>(
                LogMapper.INSTANCE.convertLogDTOs(logs),
                Messages.LOGS_LISTED
        );
    }

}
