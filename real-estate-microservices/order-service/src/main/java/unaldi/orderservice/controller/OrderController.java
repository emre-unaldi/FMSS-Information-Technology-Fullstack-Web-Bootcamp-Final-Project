package unaldi.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.service.OrderService;
import unaldi.orderservice.utils.result.DataResult;
import unaldi.orderservice.utils.result.Result;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<DataResult<OrderResponse>> save(@Valid @RequestBody OrderSaveRequest orderSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.save(orderSaveRequest));
    }

    @PutMapping
    public ResponseEntity<DataResult<OrderResponse>> update(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.update(orderUpdateRequest));
    }

    @GetMapping
    public ResponseEntity<DataResult<List<OrderResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAll());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DataResult<OrderResponse>> findById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findById(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Result> deleteById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.deleteById(orderId));
    }

}
