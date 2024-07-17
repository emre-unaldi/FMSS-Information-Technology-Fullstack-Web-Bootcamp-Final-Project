package unaldi.userservice.service.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import unaldi.userservice.entity.Account;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.response.AccountResponse;
import unaldi.userservice.utils.rabbitMQ.dto.OrderDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "advertCount", expression = "java(Integer.valueOf(10))"),
            @Mapping(target = "expirationDate", expression = "java(setExpirationDate())"),
            @Mapping(target = "isSubscribe", expression = "java(Boolean.FALSE)")
    })
    Account accountSaveRequestToAccount(AccountSaveRequest accountSaveRequest);

    default LocalDate setExpirationDate() {
        return LocalDate.now();
    }

    @Mappings({
            @Mapping(target = "id", expression = "java(foundAccount.getId())"),
            @Mapping(target = "userId", expression = "java(foundAccount.getUserId())"),
            @Mapping(target = "expirationDate", expression = "java(setExpirationDate(orderDTO, foundAccount.getExpirationDate()))"),
            @Mapping(target = "advertCount", expression = "java(foundAccount.getAdvertCount())"),
            @Mapping(target = "isSubscribe", expression = "java(Boolean.TRUE)"),
            @Mapping(target = "user", ignore = true)
    })
    Account orderDtoToAccount(OrderDTO orderDTO, Account foundAccount);

    default LocalDate setExpirationDate(OrderDTO orderDTO, LocalDate accountExpirationDate) {
        LocalDate orderDate = orderDTO.getOrderDate();
        LocalDate expirationDate = orderDTO.getExpirationDate();

        long daysBetween = ChronoUnit.DAYS.between(orderDate, expirationDate);

        return accountExpirationDate.plusDays(daysBetween);
    }

    AccountResponse accountToAccountResponse(Account account);

    @IterableMapping(elementTargetType = AccountResponse.class)
    List<AccountResponse> accountsToAccountResponse(List<Account> accounts);

}
