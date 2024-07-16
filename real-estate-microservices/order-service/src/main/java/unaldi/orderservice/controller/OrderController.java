package unaldi.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(
            description = "Save new order",
            summary = "Save a new order",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order Infos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OrderSaveRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "New Order to save",
                                            summary = "New save",
                                            description = "Complete the request with all available fields to save a new order",
                                            value = "{\n"
                                                    + "  \"userId\": 1,\n"
                                                    + "  \"packageCount\": 1,\n"
                                                    + "  \"price\": 500\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<OrderResponse>> save(@Valid @RequestBody OrderSaveRequest orderSaveRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.save(orderSaveRequest));
    }

    @PutMapping
    @Operation(
            description = "Update order",
            summary = "Update an order",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order Infos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = OrderUpdateRequest.class
                            ),
                            examples = {
                                    @ExampleObject(
                                            name = "Order",
                                            summary = "Update",
                                            description = "Update the information of an existing order",
                                            value = "{\n"
                                                    + "  \"id\": 1,\n"
                                                    + "  \"userId\": 1,\n"
                                                    + "  \"packageCount\": 5,\n"
                                                    + "  \"price\": 500\n"
                                                    + "}"
                                    )
                            }
                    )
            )
    )
    public ResponseEntity<DataResult<OrderResponse>> update(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.update(orderUpdateRequest));
    }

    @GetMapping
    @Operation(
            description = "Find all orders",
            summary = "Find all",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order Infos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<List<OrderResponse>>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findAll());
    }

    @GetMapping("/{orderId}")
    @Operation(
            description = "Find an order by id",
            summary = "Find by id",
            parameters = {
                    @Parameter(
                            name = "orderId",
                            description = "Id of the order to retrieve",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<DataResult<OrderResponse>> findById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.findById(orderId));
    }

    @DeleteMapping("/{orderId}")
    @Operation(
            description = "Delete an order by id",
            summary = "Delete an order",
            parameters = {
                    @Parameter(
                            name = "orderId",
                            description = "Id of the order to delete",
                            required = true,
                            example = "1",
                            schema = @Schema(type = "integer")
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order id",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    )
    public ResponseEntity<Result> deleteById(@PathVariable("orderId") Long orderId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.deleteById(orderId));
    }

}
