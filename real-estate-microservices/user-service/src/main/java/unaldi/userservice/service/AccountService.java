package unaldi.userservice.service;

import unaldi.userservice.entity.Account;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.response.AccountResponse;
import unaldi.userservice.utils.rabbitMQ.dto.OrderDTO;
import unaldi.userservice.utils.result.DataResult;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
public interface AccountService {

    Account save(AccountSaveRequest accountSaveRequest);
    void updateWithOrder(OrderDTO orderDTO);
    DataResult<List<AccountResponse>> findAll();
    DataResult<AccountResponse> findById(Long accountId);
    Account findByUserId(Long userId);

}
