package unaldi.orderservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.utils.client.dto.response.UserResponse;
import unaldi.orderservice.entity.Package;
import unaldi.orderservice.utils.rabbitMQ.dto.OrderDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "price", expression = "java(foundPackage.getPrice())"),
            @Mapping(target = "totalPrice", expression = "java(request.getPackageCount().doubleValue() * foundPackage.getPrice())"),
            @Mapping(target = "orderDate", expression = "java(setOrderDate())"),
            @Mapping(target = "expirationDate", expression = "java(setExpirationDate(request.getPackageCount(), foundPackage.getPackageTime()))")
    })
    Order orderSaveRequestToOrder(OrderSaveRequest request, Package foundPackage);

    @Mappings({
            @Mapping(target = "id", source = "request.id"),
            @Mapping(target = "price", expression = "java(foundPackage.getPrice())"),
            @Mapping(target = "totalPrice", expression = "java(request.getPackageCount().doubleValue() * foundPackage.getPrice())"),
            @Mapping(target = "orderDate", expression = "java(setOrderDate())"),
            @Mapping(target = "expirationDate", expression = "java(setExpirationDate(request.getPackageCount(), foundPackage.getPackageTime()))")
    })
    Order orderUpdateRequestToOrder(OrderUpdateRequest request, Package foundPackage);

    default LocalDate setOrderDate() {
        return LocalDate.now();
    }

    default LocalDate setExpirationDate(Integer packageCount, Long packageTime) {
        LocalDateTime currentDate = LocalDateTime.now();
        currentDate = currentDate.plusSeconds(packageCount * packageTime);

        return currentDate.toLocalDate();
    }

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", source = "order.id")
    OrderResponse orderToOrderResponse(Order order, UserResponse user);

    @IterableMapping(elementTargetType = OrderResponse.class)
    List<OrderResponse> ordersToOrderResponses(List<Order> orders);

    OrderDTO orderToOrderDTO(Order order);

}
