package unaldi.orderservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.repository.OrderRepository;
import unaldi.orderservice.service.OrderService;
import unaldi.orderservice.service.mapper.OrderMapper;
import unaldi.orderservice.utils.constants.ExceptionMessages;
import unaldi.orderservice.utils.constants.Messages;
import unaldi.orderservice.utils.exception.OrderNotFoundException;
import unaldi.orderservice.utils.result.DataResult;
import unaldi.orderservice.utils.result.Result;
import unaldi.orderservice.utils.result.SuccessDataResult;
import unaldi.orderservice.utils.result.SuccessResult;

import java.util.List;

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

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public DataResult<OrderResponse> save(OrderSaveRequest request) {
        Order order = OrderMapper.INSTANCE.orderSaveRequestToOrder(request);
        orderRepository.save(order);

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order),
                Messages.ORDER_SAVED
        );
    }

    @Override
    public DataResult<OrderResponse> update(OrderUpdateRequest request) {
        if (!orderRepository.existsById(request.getId())) {
            throw new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND);
        }

        Order order = OrderMapper.INSTANCE.orderUpdateRequestToOrder(request);
        orderRepository.save(order);

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order),
                Messages.ORDER_UPDATED
        );
    }

    @Override
    public DataResult<List<OrderResponse>> findAll() {
        List<Order> orders = orderRepository.findAll();

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.ordersToOrderResponses(orders),
                Messages.ORDERS_LISTED
        );
    }

    @Override
    public DataResult<OrderResponse> findById(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND));

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order),
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

}
