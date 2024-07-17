package unaldi.userservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unaldi.userservice.entity.Account;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.response.AccountResponse;
import unaldi.userservice.repository.AccountRepository;
import unaldi.userservice.repository.UserRepository;
import unaldi.userservice.service.AccountService;
import unaldi.userservice.service.mapper.AccountMapper;
import unaldi.userservice.utils.constants.Caches;
import unaldi.userservice.utils.constants.ExceptionMessages;
import unaldi.userservice.utils.constants.Messages;
import unaldi.userservice.utils.exception.AccountNotFoundException;
import unaldi.userservice.utils.exception.UserNotFoundException;
import unaldi.userservice.utils.rabbitMQ.dto.OrderDTO;
import unaldi.userservice.utils.result.DataResult;
import unaldi.userservice.utils.result.SuccessDataResult;

import java.util.List;

/**
 * Copyright (c) 2024
 * All rights reserved.
 *
 * @author Emre Ünaldı
 * @since 16.07.2024
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, CacheManager cacheManager) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.cacheManager = cacheManager;
    }

    @Override
    public Account save(AccountSaveRequest accountSaveRequest) {
        Account account = AccountMapper.INSTANCE.accountSaveRequestToAccount(accountSaveRequest);
        accountRepository.save(account);

        return account;
    }

    @Transactional
    @Override
    public void updateWithOrder(OrderDTO orderDTO) {
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        Account accountDto = accountRepository
                .findById(user.getAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));

        Account account = AccountMapper.INSTANCE.orderDtoToAccount(orderDTO, accountDto);
        accountRepository.save(account);

        userCacheClean(user.getId());
    }

    @Override
    public DataResult<List<AccountResponse>> findAll() {
        List<Account> accounts = accountRepository.findAll();

        return new SuccessDataResult<>(
                AccountMapper.INSTANCE.accountsToAccountResponse(accounts),
                Messages.ACCOUNTS_LISTED
        );
    }

    @Override
    public DataResult<AccountResponse> findById(Long accountId) {
        AccountResponse accountResponse = accountRepository
                .findById(accountId)
                .map(AccountMapper.INSTANCE::accountToAccountResponse)
                .orElseThrow(() -> new RuntimeException(ExceptionMessages.ACCOUNT_NOT_FOUND));


        return new SuccessDataResult<>(accountResponse, Messages.ACCOUNT_FOUND);
    }

    @Override
    public Account findByUserId(Long userId) {
        return accountRepository
                .findByUserId(userId)
                .orElseThrow(() -> new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND));
    }

    private void userCacheClean(Long userId) {
        Cache foundUserCache = cacheManager.getCache(Caches.USER_CACHE);
        Cache foundUsersCache = cacheManager.getCache(Caches.USERS_CACHE);

        if (foundUserCache != null) {
            foundUserCache.evict(userId);
        }

        if (foundUsersCache != null) {
            foundUsersCache.evict("all");
        }
    }

}
