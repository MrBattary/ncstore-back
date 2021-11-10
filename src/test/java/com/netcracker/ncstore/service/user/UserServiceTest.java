/*
package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.role.IRoleService;
import com.netcracker.ncstore.util.validator.EmailValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {
    private IUserService userService;

    @Mock
    private AutoCloseable closeable;

    @Mock
    private IRoleService roleServiceMocked;

    @Mock
    private UserRepository userRepositoryMocked;

    @Mock
    private PasswordEncoder passwordEncoderMocked;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        userService = new UserService(roleServiceMocked, userRepositoryMocked, passwordEncoderMocked);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void checkUserEmail() {
        try (MockedStatic<EmailValidator> emailValidatorMockedStatic = Mockito.mockStatic(EmailValidator.class)) {
            Mockito.doReturn(false).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.when(() -> EmailValidator.isEmailValid(Mockito.anyString())).thenReturn(true);

            userService.checkUserEmail("mail@gmail.com");
            Mockito.verify(userRepositoryMocked, Mockito.times(1)).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.verify(() -> EmailValidator.isEmailValid(Mockito.anyString()), Mockito.times(1));
        }
    }

    @Test
    void checkUserEmailInvalidException() {
        try (MockedStatic<EmailValidator> emailValidatorMockedStatic = Mockito.mockStatic(EmailValidator.class)) {
            emailValidatorMockedStatic.when(() -> EmailValidator.isEmailValid(Mockito.anyString())).thenReturn(false);

            assertThrows(UserServiceValidationException.class, () -> userService.checkUserEmail("mail@gmail.com"));

            Mockito.verify(userRepositoryMocked, Mockito.times(0)).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.verify(() -> EmailValidator.isEmailValid(Mockito.anyString()), Mockito.times(1));
        }
    }

    @Test
    void checkUserEmailAlreadyExistException() {
        try (MockedStatic<EmailValidator> emailValidatorMockedStatic = Mockito.mockStatic(EmailValidator.class)) {
            Mockito.doReturn(true).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.when(() -> EmailValidator.isEmailValid(Mockito.anyString())).thenReturn(true);

            assertThrows(UserServiceRepositoryException.class, () -> userService.checkUserEmail("mail@gmail.com"));

            Mockito.verify(userRepositoryMocked, Mockito.times(1)).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.verify(() -> EmailValidator.isEmailValid(Mockito.anyString()), Mockito.times(1));
        }
    }

    @Test
    void buildUserFromUserModelDTO() {
        List<Role> roleListEmpty = new ArrayList<>();
        UserModelWithoutIdDTO userModelWithoutIdDTO = new UserModelWithoutIdDTO("mail@gmail.com", "a", 0, null);

        Mockito.doReturn(false).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.doReturn("aEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doReturn(roleListEmpty).when(roleServiceMocked).buildRolesList(Mockito.anyList());

        User user = userService.buildUserFromUserModelDTO(userModelWithoutIdDTO);
        assertEquals("mail@gmail.com", user.getEmail());
        assertEquals("aEncoded", user.getPassword());
        assertEquals(0.0, user.getBalance());
        assertEquals(0, user.getRoles().size());

        Mockito.verify(userRepositoryMocked, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoderMocked, Mockito.times(1)).encode(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.times(1)).buildRolesList(Mockito.any());
    }

    @Test
    void buildUserFromUserModelDTOValidationException() {
        List<Role> roleListEmpty = new ArrayList<>();
        UserModelWithoutIdDTO userModelWithoutIdDTO = new UserModelWithoutIdDTO("mail", "a", 0, null);

        Mockito.doReturn("aEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doReturn(roleListEmpty).when(roleServiceMocked).buildRolesList(Mockito.anyList());

        assertThrows(UserServiceBuildingException.class, () -> userService.buildUserFromUserModelDTO(userModelWithoutIdDTO));

        Mockito.verify(userRepositoryMocked, Mockito.times(0)).existsByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoderMocked, Mockito.times(0)).encode(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.times(0)).buildRolesList(Mockito.anyList());
    }

    @Test
    void buildUserFromUserModelDTORepositoryException() {
        List<Role> roleListEmpty = new ArrayList<>();
        UserModelWithoutIdDTO userModelWithoutIdDTO = new UserModelWithoutIdDTO("mail@gmail.com", "a", 0, null);

        Mockito.doReturn(true).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.doReturn("aEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doReturn(roleListEmpty).when(roleServiceMocked).buildRolesList(Mockito.anyList());

        assertThrows(UserServiceBuildingException.class, () -> userService.buildUserFromUserModelDTO(userModelWithoutIdDTO));

        Mockito.verify(userRepositoryMocked, Mockito.times(1)).existsByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoderMocked, Mockito.times(0)).encode(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.times(0)).buildRolesList(Mockito.anyList());
    }
}*/
