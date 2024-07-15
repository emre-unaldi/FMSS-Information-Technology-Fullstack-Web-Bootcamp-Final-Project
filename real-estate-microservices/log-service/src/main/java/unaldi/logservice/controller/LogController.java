package unaldi.logservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.logservice.entity.dto.request.LogSaveRequest;
import unaldi.logservice.entity.dto.request.LogUpdateRequest;
import unaldi.logservice.entity.dto.response.LogResponse;
import unaldi.logservice.service.LogService;
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
@RestController
@RequestMapping("api/v1/logs")
@Tag(name = "Log Controller", description = "Log Management")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new log record",
            description = "Create a new log record with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Log details",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = LogSaveRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "New log record",
                                            summary = "New log",
                                            description = "Complete request with all available fields for a new log record",
                                            value = "{\n"
                                                    + "  \"serviceName\": \"user-service\",\n"
                                                    + "  \"httpRequestMethod\": \"GET\",\n"
                                                    + "  \"logType\": \"INFO\",\n"
                                                    + "  \"message\": \"Users in the database are listed\",\n"
                                                    + "  \"requestPath\": \"/api/v1/users\",\n"
                                                    + "  \"timestamp\": \"2024-07-16T12:00:00\",\n"
                                                    + "  \"exception\": \"NullPointerException\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<LogResponse>> save(@Valid @RequestBody LogSaveRequest logSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(logService.save(logSaveRequest));
    }

    @PutMapping
    @Operation(
            summary = "Update an existing log record",
            description = "Update an existing log record with the provided details",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Log details including id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = LogUpdateRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Log record",
                                            summary = "Update",
                                            description = "Update the information of an existing log record",
                                            value = "{\n"
                                                    + "  \"id\": \"669470d6b8f957410e4e8f1c\",\n"
                                                    + "  \"serviceName\": \"photo-service\",\n"
                                                    + "  \"httpRequestMethod\": \"POST\",\n"
                                                    + "  \"logType\": \"ERROR\",\n"
                                                    + "  \"message\": \"Users in the database are listed 1\",\n"
                                                    + "  \"requestPath\": \"/api/v1/users/1\",\n"
                                                    + "  \"timestamp\": \"2024-07-20T12:00:00\",\n"
                                                    + "  \"exception\": \"NullPointerException\"\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<LogResponse>> update(@Valid @RequestBody LogUpdateRequest logUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.update(logUpdateRequest));
    }

    @DeleteMapping("/{logId}")
    @Operation(
            summary = "Delete log record by id",
            description = "Delete a log record by its id",
            parameters = {
                    @Parameter(
                            name = "logId",
                            description = "Id of the log record to delete",
                            required = true,
                            example = "669470d6b8f957410e4e8f1c",
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Log Id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> deleteById(@PathVariable String logId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.deleteById(logId));
    }

    @GetMapping("/{logId}")
    @Operation(
            summary = "Find log record by id",
            description = "Retrieve details of a log record by its ID",
            parameters = {
                    @Parameter(
                            name = "logId",
                            description = "Id of the log record to retrieve",
                            required = true,
                            example = "669470d6b8f957410e4e8f1c",
                            schema = @Schema(type = "string")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Log Id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<LogResponse>> findById(@PathVariable String logId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.findById(logId));
    }

    @GetMapping
    @Operation(
            summary = "Find all log records",
            description = "Retrieve a list of all log records",
            requestBody =@io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Logs Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<LogResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.findAll());
    }

}
