package unaldi.advertservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.advertservice.entity.dto.request.AdvertSaveRequest;
import unaldi.advertservice.entity.dto.request.AdvertUpdateRequest;
import unaldi.advertservice.entity.dto.response.AdvertResponse;
import unaldi.advertservice.service.AdvertService;
import unaldi.advertservice.utils.result.DataResult;
import unaldi.advertservice.utils.result.Result;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 13.07.2024
 */
@RestController
@RequestMapping("/api/v1/adverts")
public class AdvertController {

    private final AdvertService advertService;

    @Autowired
    public AdvertController(AdvertService advertService) {
        this.advertService = advertService;
    }

    @PostMapping
    public ResponseEntity<DataResult<AdvertResponse>> save(@Valid @RequestBody AdvertSaveRequest advertSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(advertService.save(advertSaveRequest));
    }

    @PutMapping
    public ResponseEntity<DataResult<AdvertResponse>> update(@Valid @RequestBody AdvertUpdateRequest advertUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.update(advertUpdateRequest));
    }

    @GetMapping
    public ResponseEntity<DataResult<List<AdvertResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.findAll());
    }

    @GetMapping("/{advertId}")
    public ResponseEntity<DataResult<AdvertResponse>> findById(@PathVariable("advertId") Long advertId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.findById(advertId));
    }

    @DeleteMapping("/{advertId}")
    public ResponseEntity<Result> deleteById(@PathVariable("advertId") Long advertId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(advertService.deleteById(advertId));
    }

}
