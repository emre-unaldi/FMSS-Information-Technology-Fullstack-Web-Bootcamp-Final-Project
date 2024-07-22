package unaldi.userservice.service.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import unaldi.userservice.entity.Account;
import unaldi.userservice.entity.ERole;
import unaldi.userservice.entity.Role;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.request.UserUpdateRequest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectFactory {
    private static ObjectFactory instance;
    private User user;
    private Set<Role> roles;
    private SignUpRequest signUpRequest;
    private UserUpdateRequest userUpdateRequest;
    private Account account;

    public static ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public User getUser() {
        if (user == null) {
            user = new User();
            user.setId(1L);
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setUsername("johnDoe");
            user.setEmail("johndoe@example.com");
            user.setPassword("password");
            user.setPhoneNumber("1234567890");
            user.setRoles(getRoles());
            user.setAccount(getAccount());
        }
        return user;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
            roles.add(new Role(1, ERole.ROLE_USER));
            roles.add(new Role(2, ERole.ROLE_MODERATOR));
            roles.add(new Role(3, ERole.ROLE_ADMIN));
        }
        return roles;
    }


    public List<User> getUserList() {
        return List.of(getUser());
    }

    public SignUpRequest getSignUpRequest() {
        if (signUpRequest == null) {
            signUpRequest = new SignUpRequest();
            signUpRequest.setFirstName("John");
            signUpRequest.setLastName("Doe");
            signUpRequest.setUsername("johnDoe");
            signUpRequest.setEmail("johndoe@example.com");
            signUpRequest.setPassword("password");
            signUpRequest.setPhoneNumber("1234567890");
            signUpRequest.setRoles(Set.of(
                    String.valueOf(ERole.ROLE_USER),
                    String.valueOf(ERole.ROLE_ADMIN),
                    String.valueOf(ERole.ROLE_MODERATOR))
            );
        }
        return signUpRequest;
    }

    public UserUpdateRequest getUserUpdateRequest() {
        if (userUpdateRequest == null) {
            userUpdateRequest = new UserUpdateRequest();
            userUpdateRequest.setId(1L);
            userUpdateRequest.setFirstName("John");
            userUpdateRequest.setLastName("Doe");
            userUpdateRequest.setUsername("johnDoe");
            userUpdateRequest.setEmail("johndoe@example.com");
            userUpdateRequest.setPassword("password");
            userUpdateRequest.setPhoneNumber("1234567890");
            userUpdateRequest.setRoles(Set.of(
                    String.valueOf(ERole.ROLE_USER),
                    String.valueOf(ERole.ROLE_ADMIN),
                    String.valueOf(ERole.ROLE_MODERATOR))
            );
        }
        return userUpdateRequest;
    }

    public Account getAccount() {
        if (account == null) {
            account = new Account(
                    1L,
                    1L,
                    10,
                    LocalDate.now().plusDays(30),
                    true,
                    null
            );
        }
        return account;
    }
}
