package unaldi.logservice.controller;

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
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<DataResult<LogResponse>> save(@Valid @RequestBody LogSaveRequest logSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(logService.save(logSaveRequest));
    }

    @PutMapping
    public ResponseEntity<DataResult<LogResponse>> update(@Valid @RequestBody LogUpdateRequest logUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.update(logUpdateRequest));
    }

    @DeleteMapping("/{logId}")
    public ResponseEntity<Result> deleteById(@PathVariable String logId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.deleteById(logId));
    }

    @GetMapping("/{logId}")
    public ResponseEntity<DataResult<LogResponse>> findById(@PathVariable String logId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.findById(logId));
    }

    @GetMapping
    public ResponseEntity<DataResult<List<LogResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(logService.findAll());
    }

}
