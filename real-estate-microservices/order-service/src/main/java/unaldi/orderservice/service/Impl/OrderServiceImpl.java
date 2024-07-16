package unaldi.orderservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.dto.EmailDetailsDTO;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.repository.OrderRepository;
import unaldi.orderservice.service.EmailService;
import unaldi.orderservice.service.OrderService;
import unaldi.orderservice.service.mapper.OrderMapper;
import unaldi.orderservice.utils.client.UserServiceClient;
import unaldi.orderservice.utils.client.dto.response.RestResponse;
import unaldi.orderservice.utils.client.dto.response.UserResponse;
import unaldi.orderservice.utils.constants.Caches;
import unaldi.orderservice.utils.constants.ExceptionMessages;
import unaldi.orderservice.utils.constants.Messages;
import unaldi.orderservice.utils.exception.OrderNotFoundException;
import unaldi.orderservice.utils.result.DataResult;
import unaldi.orderservice.utils.result.Result;
import unaldi.orderservice.utils.result.SuccessDataResult;
import unaldi.orderservice.utils.result.SuccessResult;

import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private final EmailService emailService;
    private final CacheManager cacheManager;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserServiceClient userServiceClient, EmailService emailService, CacheManager cacheManager) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.emailService = emailService;
        this.cacheManager = cacheManager;
    }

    @CacheEvict(value = Caches.ORDERS_CACHE, allEntries = true, condition = "#result.success != false")
    @Override
    public DataResult<OrderResponse> save(OrderSaveRequest request) {
        UserResponse userResponse = fetchUser(request.getUserId());
        Order order = OrderMapper.INSTANCE.orderSaveRequestToOrder(request);
        orderRepository.save(order);

        OrderResponse orderResponse = OrderMapper.INSTANCE.orderToOrderResponse(order, userResponse);
        emailService.sendEmail(prepareEmailDetailsDTO(orderResponse));

        return new SuccessDataResult<>(orderResponse, Messages.ORDER_SAVED);
    }


    @Caching(
            put = { @CachePut(value = Caches.ORDER_CACHE, key = "#request.getId()", unless = "#result.success != true") },
            evict = { @CacheEvict(value = Caches.ORDERS_CACHE, allEntries = true, condition = "#result.success != false") }
    )
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

    @Cacheable(value = Caches.ORDERS_CACHE, key = "'all'", unless = "#result.success != true")
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

    @Cacheable(value = Caches.ORDER_CACHE, key = "#orderId", unless = "#result.success != true")
    @Override
    public DataResult<OrderResponse> findById(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ExceptionMessages.ORDER_NOT_FOUND));

        UserResponse userResponse = fetchUser(order.getUserId());

        return new SuccessDataResult<>(
                OrderMapper.INSTANCE.orderToOrderResponse(order, userResponse),
                Messages.ORDER_FOUND
        );
    }

    @Caching(
            evict = {
                    @CacheEvict(value = Caches.ORDERS_CACHE, allEntries = true, condition = "#result.success != false"),
                    @CacheEvict(value = Caches.ORDER_CACHE, key = "#orderId", condition = "#result.success != false")
            }
    )
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

    private EmailDetailsDTO prepareEmailDetailsDTO(OrderResponse order) {
        String orderDetails = String.format(
                "Order Details%n" +
                        "Order number = %d%n" +
                        "User = %s %s%n" +
                        "Number of packages purchased = %d%n" +
                        "Price = %f%n" +
                        "Total Price = %f%n" +
                        "Order Date = %s%n" +
                        "Package Expiration Time = %s",
                order.getId(),
                order.getUser().getFirstName(),
                order.getUser().getLastName(),
                order.getPackageCount(),
                order.getPrice(),
                order.getTotalPrice(),
                order.getOrderDate(),
                order.getExpirationDate()
        );

        return EmailDetailsDTO.builder()
                .recipient(order.getUser().getEmail())
                .subject(Messages.ORDER_MAIL_SUBJECT)
                .body(orderDetails)
                .build();
    }

}
