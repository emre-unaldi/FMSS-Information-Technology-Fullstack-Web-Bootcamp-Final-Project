package unaldi.orderservice.service;

import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
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
public interface OrderService {

    DataResult<OrderResponse> save(OrderSaveRequest orderSaveRequest);
    DataResult<OrderResponse> update(OrderUpdateRequest orderUpdateRequest);
    DataResult<List<OrderResponse>> findAll();
    DataResult<OrderResponse> findById(Long id);
    Result deleteById(Long orderId);

}
