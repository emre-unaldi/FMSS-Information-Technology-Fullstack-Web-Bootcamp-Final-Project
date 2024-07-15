package unaldi.logservice.service;

import unaldi.logservice.entity.dto.request.LogSaveRequest;
import unaldi.logservice.entity.dto.request.LogUpdateRequest;
import unaldi.logservice.entity.dto.response.LogResponse;
import unaldi.logservice.utils.result.DataResult;
import unaldi.logservice.utils.result.Result;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
public interface LogService {

    DataResult<LogResponse> save(LogSaveRequest logSaveRequest);
    DataResult<LogResponse> update(LogUpdateRequest logUpdateRequest);
    Result deleteById(String logId);
    DataResult<LogResponse> findById(String logId);
    DataResult<List<LogResponse>> findAll();

}
