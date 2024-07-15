package unaldi.logservice.service.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import unaldi.logservice.entity.Log;
import unaldi.logservice.entity.dto.request.LogSaveRequest;
import unaldi.logservice.entity.dto.request.LogUpdateRequest;
import unaldi.logservice.entity.dto.response.LogResponse;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 15.07.2024
 */
@Mapper(componentModel = "spring")
public interface LogMapper {

    LogMapper INSTANCE = Mappers.getMapper( LogMapper.class );

    @Mapping(target = "id", ignore = true)
    Log logSaveRequestToLog(LogSaveRequest logSaveRequest);

    Log logUpdateRequestToLog(LogUpdateRequest logUpdateRequest);

    LogResponse logToLogResponse(Log log);

    Log logResponseToLog(LogResponse logResponse);

    @IterableMapping(elementTargetType = LogResponse.class)
    List<LogResponse> convertLogDTOs(List<Log> logs);

}