package unaldi.advertservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.advertservice.entity.dto.request.AddressSaveRequest;
import unaldi.advertservice.entity.dto.request.AddressUpdateRequest;
import unaldi.advertservice.entity.dto.response.AddressResponse;
import unaldi.advertservice.service.AddressService;
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
@RequestMapping("/api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<DataResult<AddressResponse>> save(@Valid @RequestBody AddressSaveRequest addressSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressService.save(addressSaveRequest));
    }

    @PutMapping
    public ResponseEntity<DataResult<AddressResponse>> update(@Valid @RequestBody AddressUpdateRequest addressUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.update(addressUpdateRequest));
    }

    @GetMapping
    public ResponseEntity<DataResult<List<AddressResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.findAll());
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<DataResult<AddressResponse>> findById(@PathVariable("addressId") Long addressId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.findById(addressId));
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Result> deleteById(@PathVariable("addressId") Long addressId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addressService.deleteById(addressId));
    }

}
