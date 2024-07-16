package unaldi.orderservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import unaldi.orderservice.entity.Order;
import unaldi.orderservice.entity.dto.request.OrderSaveRequest;
import unaldi.orderservice.entity.dto.request.OrderUpdateRequest;
import unaldi.orderservice.entity.dto.response.OrderResponse;
import unaldi.orderservice.utils.client.dto.response.UserResponse;

import java.time.LocalDate;
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
            @Mapping(target = "totalPrice", expression = "java(request.getPackageCount().doubleValue() * request.getPrice())"),
            @Mapping(target = "orderDate", expression = "java(setOrderDate())"),
            @Mapping(target = "expirationDate", expression = "java(setExpirationDate(request.getPackageCount()))")
    })
    Order orderSaveRequestToOrder(OrderSaveRequest request);

    @Mappings({
            @Mapping(target = "totalPrice", expression = "java(request.getPackageCount().doubleValue() * request.getPrice())"),
            @Mapping(target = "orderDate", expression = "java(setOrderDate())"),
            @Mapping(target = "expirationDate", expression = "java(setExpirationDate(request.getPackageCount()))")
    })
    Order orderUpdateRequestToOrder(OrderUpdateRequest request);

    default LocalDate setOrderDate() {
        return LocalDate.now();
    }

    default LocalDate setExpirationDate(Integer packageCount) {
        return LocalDate.now().plusMonths(packageCount);
    }

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", source = "order.id")
    OrderResponse orderToOrderResponse(Order order, UserResponse user);

    @IterableMapping(elementTargetType = OrderResponse.class)
    List<OrderResponse> ordersToOrderResponses(List<Order> orders);

}
