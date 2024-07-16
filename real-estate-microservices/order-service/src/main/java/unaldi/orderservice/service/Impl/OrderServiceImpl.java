package unaldi.orderservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.repository.OrderRepository;
import unaldi.orderservice.service.OrderService;
import unaldi.orderservice.service.mapper.OrderMapper;
import unaldi.orderservice.utils.client.UserServiceClient;
import unaldi.orderservice.utils.client.dto.response.RestResponse;
import unaldi.orderservice.utils.client.dto.response.UserResponse;
import unaldi.orderservice.utils.constants.ExceptionMessages;
import unaldi.orderservice.utils.constants.Messages;
import unaldi.orderservice.utils.exception.OrderNotFoundException;
import unaldi.orderservice.utils.result.DataResult;
import unaldi.orderservice.utils.result.Result;
import unaldi.orderservice.utils.result.SuccessDataResult;
import unaldi.orderservice.utils.result.SuccessResult;

import java.util.List;
import java.util.Objects;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public DataResult<OrderResponse> save(OrderSaveRequest request) {
        UserResponse userResponse = fetchUser(request.getUserId());
        Order order = OrderMapper.INSTANCE.orderSaveRequestToOrder(request);
        orderRepository.save(order);

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order, userResponse),
                Messages.ORDER_SAVED
        );
    }

    @Override
    public DataResult<OrderResponse> update(OrderUpdateRequest request) {
        if (!orderRepository.existsById(request.getId())) {
            throw new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND);
        }

        UserResponse userResponse = fetchUser(request.getUserId());
        Order order = OrderMapper.INSTANCE.orderUpdateRequestToOrder(request);
        orderRepository.save(order);

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order, userResponse),
                Messages.ORDER_UPDATED
        );
    }

    @Override
    public DataResult<List<OrderResponse>> findAll() {
        List<Order> orders = orderRepository.findAll();

        List<OrderResponse> orderResponses = orders.stream()
                .map(order -> {
                    UserResponse userResponse = fetchUser(order.getUserId());
                    return OrderMapper.INSTANCE.orderToOrderResponse(order, userResponse);
                })
                .toList();

        return new SuccessDataResult<>(orderResponses, Messages.ORDERS_LISTED);
    }

    @Override
    public DataResult<OrderResponse> findById(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND));

        UserResponse userResponse = fetchUser(order.getUserId());

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order, userResponse),
                Messages.ORDER_FOUND
        );
    }

    @Override
    public Result deleteById(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND));

        orderRepository.deleteById(order.getId());

        return new SuccessResult(Messages.ORDER_DELETED);
    }

    private UserResponse fetchUser(Long userId) {
        ResponseEntity<RestResponse<UserResponse>> userResponse = userServiceClient.findById(userId);

        return Objects.requireNonNull(userResponse.getBody()).getData();
    }

}
