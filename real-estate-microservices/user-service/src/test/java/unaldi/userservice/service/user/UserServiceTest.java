package unaldi.userservice.service.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import unaldi.userservice.entity.ERole;
import unaldi.userservice.entity.Role;
import unaldi.userservice.entity.User;
import unaldi.userservice.entity.dto.request.AccountSaveRequest;
import unaldi.userservice.entity.dto.request.SignUpRequest;
import unaldi.userservice.entity.dto.request.UserUpdateRequest;
import unaldi.userservice.entity.dto.response.UserResponse;
import unaldi.userservice.repository.RoleRepository;
import unaldi.userservice.repository.UserRepository;
import unaldi.userservice.service.AccountService;
import unaldi.userservice.service.Impl.UserServiceImpl;
import unaldi.userservice.utils.exception.EmailAlreadyExistsException;
import unaldi.userservice.utils.exception.UserNotFoundException;
import unaldi.userservice.utils.exception.UsernameAlreadyExistsException;
import unaldi.userservice.utils.rabbitMQ.dto.LogDTO;
import unaldi.userservice.utils.rabbitMQ.producer.LogProducer;
import unaldi.userservice.utils.result.DataResult;
import unaldi.userservice.utils.result.Result;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static User user;
    private static List<User> userList;
    private static SignUpRequest signUpRequest;
    private static UserUpdateRequest userUpdateRequest;
    private final Long nonExistentUserId = -1L;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private LogProducer logProducer;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void setUp() {
        user = ObjectFactory.getInstance().getUser();
        userList = ObjectFactory.getInstance().getUserList();
        signUpRequest = ObjectFactory.getInstance().getSignUpRequest();
        userUpdateRequest = ObjectFactory.getInstance().getUserUpdateRequest();
    }

    @Test
    void givenUserSaveRequest_whenRegister_thenUserShouldBeRegistered() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(accountService.save(any(AccountSaveRequest.class))).thenReturn(ObjectFactory.getInstance().getAccount());
        when(roleRepository.findByName(any(ERole.class))).thenAnswer(invocation -> {
            ERole roleName = invocation.getArgument(0);
            return Optional.of(new Role(null, roleName));
        });

        DataResult<UserResponse> result = userService.register(signUpRequest);
        assertTrue(result.getSuccess(), "User registration failed");

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, times(2)).save(any(User.class));
        verify(accountService, times(1)).save(any(AccountSaveRequest.class));
        verify(roleRepository, times(signUpRequest.getRoles().size())).findByName(any(ERole.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenUserUpdateRequest_whenUpdate_thenUserShouldBeUpdated() {
        when(userRepository.existsById(userUpdateRequest.getId())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findByName(any(ERole.class))).thenAnswer(invocation -> {
            ERole roleName = invocation.getArgument(0);
            return Optional.of(new Role(null, roleName));
        });

        DataResult<UserResponse> result = userService.update(userUpdateRequest);
        assertTrue(result.getSuccess(), "User update failed");

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
        verify(roleRepository, times(userUpdateRequest.getRoles().size())).findByName(any(ERole.class));
    }

    @Test
    void givenUserId_whenDeleteById_thenUserShouldBeDeleted() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Result result = userService.deleteById(user.getId());
        assertTrue(result.getSuccess(), "User deletion failed");

        verify(userRepository, times(1)).deleteById(user.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenUserId_whenFindById_thenUserShouldBeFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        DataResult<UserResponse> result = userService.findById(user.getId());
        assertTrue(result.getSuccess(), "User not found");

        verify(userRepository, times(1)).findById(user.getId());
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenUserList_whenFindAll_thenAllUsersShouldBeReturned() {
        when(userRepository.findAll()).thenReturn(userList);

        DataResult<List<UserResponse>> result = userService.findAll();
        assertTrue(result.getSuccess(), "User list retrieval failed");

        verify(userRepository, times(1)).findAll();
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentUserUpdateRequest_whenUpdate_thenUserNotFoundExceptionShouldBeThrown() {
        when(userRepository.existsById(userUpdateRequest.getId())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.update(userUpdateRequest);
        }, "User not found exception expected");

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, never()).save(any(User.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentUserId_whenDeleteById_thenUserNotFoundExceptionShouldBeThrown() {
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteById(nonExistentUserId);
        }, "User not found exception expected");

        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(userRepository, never()).deleteById(anyLong());
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentUserId_whenFindById_thenUserNotFoundExceptionShouldBeThrown() {
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findById(nonExistentUserId);
        }, "User not found exception expected");

        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenExistingUsername_whenRegister_thenUsernameAlreadyExistsExceptionShouldBeThrown() {
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.register(signUpRequest);
        }, "Username already exists exception expected");

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenExistingEmail_whenRegister_thenEmailAlreadyExistsExceptionShouldBeThrown() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.register(signUpRequest);
        }, "Email already exists exception expected");

        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenExistingUsername_whenUpdate_thenUsernameAlreadyExistsExceptionShouldBeThrown() {
        when(userRepository.existsById(userUpdateRequest.getId())).thenReturn(true);
        when(userRepository.existsByUsername(userUpdateRequest.getUsername())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.update(userUpdateRequest);
        }, "Username already exists exception expected");

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenExistingEmail_whenUpdate_thenEmailAlreadyExistsExceptionShouldBeThrown() {
        when(userRepository.existsById(userUpdateRequest.getId())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(userUpdateRequest.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.update(userUpdateRequest);
        }, "Email already exists exception expected");

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).existsByUsername(anyString());
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentUserId_whenUpdate_thenUserNotFoundExceptionShouldBeThrown() {
        when(userRepository.existsById(userUpdateRequest.getId())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.update(userUpdateRequest);
        }, "User not found exception expected");

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenExistingUsername_whenFindByUsername_thenUserResponseShouldBeReturned() {
        String username = "testUser";
        User user = ObjectFactory.getInstance().getUser(); // Ensure user has the username

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        DataResult<UserResponse> result = userService.findByUsername(username);

        assertTrue(result.getSuccess(), "User retrieval failed");

        verify(userRepository, times(1)).findByUsername(username);
        verify(logProducer, times(1)).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNonExistentUsername_whenFindByUsername_thenUserNotFoundExceptionShouldBeThrown() {
        String username = "nonExistentUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername(username);
        }, "User not found exception expected");

        verify(userRepository, times(1)).findByUsername(username);
        verify(logProducer, never()).sendToLog(any(LogDTO.class));
    }

    @Test
    void givenNullStrRoles_whenMapToUserRoles_thenDefaultUserRoleShouldBeReturned() {
        when(roleRepository.findByName(ERole.ROLE_USER))
                .thenReturn(Optional.of(new Role(null, ERole.ROLE_USER)));

        Set<Role> roles = userService.mapToUserRoles(null);

        assertEquals(1, roles.size(), "Expected only one role to be returned");
        assertTrue(roles.stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_USER)), "Expected ROLE_USER to be present in the roles set");

        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(roleRepository, never()).findByName(ERole.ROLE_ADMIN);
        verify(roleRepository, never()).findByName(ERole.ROLE_MODERATOR);
    }

    @Test
    void givenAdminStrRole_whenMapToUserRoles_thenAdminRoleShouldBeReturned() {
        when(roleRepository.findByName(ERole.ROLE_ADMIN))
                .thenReturn(Optional.of(new Role(null, ERole.ROLE_ADMIN)));

        Set<String> strRoles = new HashSet<>();
        strRoles.add("admin");

        Set<Role> roles = userService.mapToUserRoles(strRoles);

        assertEquals(1, roles.size(), "Expected only one role to be returned");
        assertTrue(roles.stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_ADMIN)), "Expected ROLE_ADMIN to be present in the roles set");

        verify(roleRepository, times(1)).findByName(ERole.ROLE_ADMIN);
        verify(roleRepository, never()).findByName(ERole.ROLE_USER);
        verify(roleRepository, never()).findByName(ERole.ROLE_MODERATOR);
    }

    @Test
    void givenModeratorStrRole_whenMapToUserRoles_thenModeratorRoleShouldBeReturned() {
        when(roleRepository.findByName(ERole.ROLE_MODERATOR))
                .thenReturn(Optional.of(new Role(null, ERole.ROLE_MODERATOR)));

        Set<String> strRoles = new HashSet<>();
        strRoles.add("moderator");

        Set<Role> roles = userService.mapToUserRoles(strRoles);

        assertEquals(1, roles.size(), "Expected only one role to be returned");
        assertTrue(roles.stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_MODERATOR)), "Expected ROLE_MODERATOR to be present in the roles set");

        verify(roleRepository, times(1)).findByName(ERole.ROLE_MODERATOR);
        verify(roleRepository, never()).findByName(ERole.ROLE_USER);
        verify(roleRepository, never()).findByName(ERole.ROLE_ADMIN);
    }

    @Test
    void givenUnknownStrRole_whenMapToUserRoles_thenDefaultUserRoleShouldBeReturned() {
        when(roleRepository.findByName(ERole.ROLE_USER))
                .thenReturn(Optional.of(new Role(null, ERole.ROLE_USER)));

        Set<String> strRoles = new HashSet<>();
        strRoles.add("unknownRole");

        Set<Role> roles = userService.mapToUserRoles(strRoles);

        assertEquals(1, roles.size(), "Expected only one role to be returned");
        assertTrue(roles.stream()
                .anyMatch(role -> role.getName().equals(ERole.ROLE_USER)), "Expected ROLE_USER to be present in the roles set");

        verify(roleRepository, times(1)).findByName(ERole.ROLE_USER);
        verify(roleRepository, never()).findByName(ERole.ROLE_ADMIN);
        verify(roleRepository, never()).findByName(ERole.ROLE_MODERATOR);
    }
}
