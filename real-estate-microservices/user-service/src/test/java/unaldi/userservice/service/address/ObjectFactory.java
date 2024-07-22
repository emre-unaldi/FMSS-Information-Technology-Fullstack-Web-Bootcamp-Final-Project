package unaldi.userservice.service.address;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import unaldi.userservice.entity.Account;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.response.AccountResponse;
import unaldi.userservice.utils.rabbitMQ.dto.OrderDTO;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectFactory {
    private static ObjectFactory instance;
    private Account account;
    private AccountSaveRequest accountSaveRequest;
    private OrderDTO orderDTO;
    private User user;
    private AccountResponse accountResponse;
    private List<Account> accountList;

    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public Account getAccount() {
        if (account == null) {
            account = new Account();
            account.setId(1L);
            account.setUserId(1L);
            account.setAdvertCount(5);
            account.setExpirationDate(LocalDate.of(2024, 12, 31));
            account.setIsSubscribe(true);
        }
        return account;
    }

    public AccountSaveRequest getAccountSaveRequest() {
        if (accountSaveRequest == null) {
            accountSaveRequest = new AccountSaveRequest();
            accountSaveRequest.setUserId(1L);
        }
        return accountSaveRequest;
    }

    public OrderDTO getOrderDTO() {
        if (orderDTO == null) {
            orderDTO = new OrderDTO();
            orderDTO.setId(1L);
            orderDTO.setUserId(1L);
            orderDTO.setPackageCount(3);
            orderDTO.setPrice(100.0);
            orderDTO.setTotalPrice(300.0);
            orderDTO.setOrderDate(LocalDate.of(2024, 7, 22));
            orderDTO.setExpirationDate(LocalDate.of(2024, 12, 31));
        }
        return orderDTO;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
            user.setId(1L);
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setUsername("johnDoe");
            user.setEmail("john.doe@example.com");
            user.setPassword("password");
            user.setPhoneNumber("1234567890");
            user.setAccount(getAccount());
        }
        return user;
    }

    public AccountResponse getAccountResponse() {
        if (accountResponse == null) {
            accountResponse = new AccountResponse();
            accountResponse.setId(1L);
            accountResponse.setUserId(1L);
            accountResponse.setAdvertCount(5);
            accountResponse.setExpirationDate(LocalDate.of(2024, 12, 31));
            accountResponse.setIsSubscribe(true);
        }
        return accountResponse;
    }

    public List<Account> getAccountList() {
        if (accountList == null) {
            Account accountOne = new Account();
            accountOne.setId(1L);
            accountOne.setUserId(1L);
            accountOne.setAdvertCount(5);
            accountOne.setExpirationDate(LocalDate.of(2024, 12, 31));
            accountOne.setIsSubscribe(true);

            Account accountTwo = new Account();
            accountTwo.setId(2L);
            accountTwo.setUserId(2L);
            accountTwo.setAdvertCount(3);
            accountTwo.setExpirationDate(LocalDate.of(2024, 11, 30));
            accountTwo.setIsSubscribe(false);

            accountList = List.of(accountOne, accountTwo);
        }
        return accountList;
    }
}
