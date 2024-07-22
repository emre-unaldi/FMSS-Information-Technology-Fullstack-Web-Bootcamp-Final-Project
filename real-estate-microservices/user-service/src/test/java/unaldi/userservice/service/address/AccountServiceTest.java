package unaldi.userservice.service.address;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import unaldi.userservice.entity.Account;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.response.AccountResponse;
import unaldi.userservice.repository.AccountRepository;
import unaldi.userservice.repository.UserRepository;
import unaldi.userservice.service.Impl.AccountServiceImpl;
import unaldi.userservice.utils.constants.Caches;
import unaldi.userservice.utils.exception.AccountNotFoundException;
import unaldi.userservice.utils.rabbitMQ.dto.OrderDTO;
import unaldi.userservice.utils.result.DataResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private static Account account;
    private static AccountSaveRequest accountSaveRequest;
    private static OrderDTO orderDTO;
    private static User user;
    private static AccountResponse accountResponse;
    private static List<Account> accountList;
    private final Long nonExistentAccountId = -1L;
    private final Long nonExistentUserId = -1L;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeAll
    static void setUp() {
        account = ObjectFactory.getInstance().getAccount();
        accountSaveRequest = ObjectFactory.getInstance().getAccountSaveRequest();
        orderDTO = ObjectFactory.getInstance().getOrderDTO();
        user = ObjectFactory.getInstance().getUser();
        accountResponse = ObjectFactory.getInstance().getAccountResponse();
        accountList = ObjectFactory.getInstance().getAccountList();
    }

    @Test
    void givenAccountSaveRequest_whenSave_thenAccountShouldBeSaved() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.save(accountSaveRequest);
        assertNotNull(result, "Expected saved account to be not null");
        assertEquals(account.getUserId(), result.getUserId(), "Expected userId to match");

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void givenOrderDTO_whenUpdateWithOrder_thenAccountShouldBeUpdated() {
        when(userRepository.findById(orderDTO.getUserId())).thenReturn(Optional.of(user));
        when(accountRepository.findById(user.getAccount().getId())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.updateWithOrder(orderDTO);

        verify(userRepository, times(1)).findById(orderDTO.getUserId());
        verify(accountRepository, times(1)).findById(user.getAccount().getId());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(cacheManager, times(1)).getCache(Caches.USER_CACHE);
        verify(cacheManager, times(1)).getCache(Caches.USERS_CACHE);
    }

    @Test
    void givenAccountList_whenFindAll_thenAllAccountsShouldBeReturned() {
        when(accountRepository.findAll()).thenReturn(accountList);

        DataResult<List<AccountResponse>> result = accountService.findAll();
        assertTrue(result.getSuccess(), "Expected result to be successful");
        assertEquals(accountList.size(), result.getData().size(), "Expected account list size to match");

        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void givenAccountId_whenFindById_thenAccountShouldBeFound() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        DataResult<AccountResponse> result = accountService.findById(account.getId());
        assertTrue(result.getSuccess(), "Expected result to be successful");
        assertEquals(accountResponse.getId(), result.getData().getId(), "Expected account ID to match");

        verify(accountRepository, times(1)).findById(account.getId());
    }

    @Test
    void givenUserId_whenFindByUserId_thenAccountShouldBeFound() {
        when(accountRepository.findByUserId(user.getId())).thenReturn(Optional.of(account));

        Account result = accountService.findByUserId(user.getId());
        assertNotNull(result, "Expected account to be not null");
        assertEquals(account.getId(), result.getId(), "Expected account ID to match");

        verify(accountRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void givenNonExistentAccountId_whenFindById_thenAccountNotFoundExceptionShouldBeThrown() {
        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.findById(nonExistentAccountId);
        }, "Expected AccountNotFoundException to be thrown");

        verify(accountRepository, times(1)).findById(nonExistentAccountId);
    }

    @Test
    void givenNonExistentUserId_whenFindByUserId_thenAccountNotFoundExceptionShouldBeThrown() {
        when(accountRepository.findByUserId(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.findByUserId(nonExistentUserId);
        }, "Expected AccountNotFoundException to be thrown");

        verify(accountRepository, times(1)).findByUserId(nonExistentUserId);
    }
}
