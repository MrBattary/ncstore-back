package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.exception.UserServiceCreationException;
import com.netcracker.ncstore.exception.UserServiceRepositoryException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.payment.IPaymentService;
import com.netcracker.ncstore.service.role.IRoleService;
import com.netcracker.ncstore.util.validator.EmailValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private PersonRepository personRepositoryMocked;

    @Mock
    private CompanyRepository companyRepositoryMocked;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IPaymentService paymentService;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        userService = new UserService(roleServiceMocked, userRepositoryMocked, personRepositoryMocked, companyRepositoryMocked, passwordEncoder, paymentService);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void checkNewUserEmail() {
        try (MockedStatic<EmailValidator> emailValidatorMockedStatic = Mockito.mockStatic(EmailValidator.class)) {
            Mockito.doReturn(false).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.when(() -> EmailValidator.isEmailValid(Mockito.anyString())).thenReturn(true);

            userService.checkNewUserEmail("mail@gmail.com");
            Mockito.verify(userRepositoryMocked).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.verify(() -> EmailValidator.isEmailValid(Mockito.anyString()));
        }
    }

    @Test
    void checkNewUserEmailInvalidException() {
        try (MockedStatic<EmailValidator> emailValidatorMockedStatic = Mockito.mockStatic(EmailValidator.class)) {
            emailValidatorMockedStatic.when(() -> EmailValidator.isEmailValid(Mockito.anyString())).thenReturn(false);

            assertThrows(UserServiceValidationException.class, () -> userService.checkNewUserEmail("mail@gmail.com"));

            Mockito.verify(userRepositoryMocked, Mockito.never()).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.verify(() -> EmailValidator.isEmailValid(Mockito.anyString()));
        }
    }

    @Test
    void checkNewUserEmailAlreadyExistException() {
        try (MockedStatic<EmailValidator> emailValidatorMockedStatic = Mockito.mockStatic(EmailValidator.class)) {
            Mockito.doReturn(true).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.when(() -> EmailValidator.isEmailValid(Mockito.anyString())).thenReturn(true);

            assertThrows(UserServiceRepositoryException.class, () -> userService.checkNewUserEmail("mail@gmail.com"));

            Mockito.verify(userRepositoryMocked).existsByEmail(Mockito.anyString());
            emailValidatorMockedStatic.verify(() -> EmailValidator.isEmailValid(Mockito.anyString()));
        }
    }


    @Test
    void createPersonFromRequest() {
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("mail@gmail.com", "p", "n", "f", "l", null, new ArrayList<>());

        Mockito.doReturn(false).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.doReturn(new ArrayList<>()).when(roleServiceMocked).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.when(personRepositoryMocked.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.createPersonFromRequest(signUpPersonRequest);

        Mockito.verify(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.verify(roleServiceMocked).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.verify(personRepositoryMocked).save(Mockito.any());
    }

    @Test
    void createPersonFromRequestValidationException() {
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("mail", "p", "n", "f", "l", null, null);

        assertThrows(UserServiceCreationException.class, () -> userService.createPersonFromRequest(signUpPersonRequest));

        Mockito.verify(userRepositoryMocked, Mockito.never()).existsByEmail(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.never()).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.verify(personRepositoryMocked, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createPersonFromRequestRepositoryException() {
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("mail@gmail.com", "p", "n", "f", "l", null, null);

        Mockito.doReturn(true).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.doReturn(null).when(roleServiceMocked).parseRoleNamesListToRolesList(Mockito.anyList());

        assertThrows(UserServiceCreationException.class, () -> userService.createPersonFromRequest(signUpPersonRequest));

        Mockito.verify(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.never()).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.verify(personRepositoryMocked, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createCompanyFromRequest() {
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("mail@gmail.com", "p", "n", null, new ArrayList<>());

        Mockito.doReturn(false).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.doReturn(new ArrayList<>()).when(roleServiceMocked).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.when(personRepositoryMocked.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.createCompanyFromRequest(signUpCompanyRequest);

        Mockito.verify(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.verify(roleServiceMocked).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.verify(companyRepositoryMocked).save(Mockito.any());
    }

    @Test
    void createCompanyFromRequestValidationException() {
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("mail", "p", "n", null, null);

        assertThrows(UserServiceCreationException.class, () -> userService.createCompanyFromRequest(signUpCompanyRequest));

        Mockito.verify(userRepositoryMocked, Mockito.never()).existsByEmail(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.never()).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.verify(companyRepositoryMocked, Mockito.never()).save(Mockito.any());
    }

    @Test
    void createCompanyFromRequestRepositoryException() {
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("mail@gmail.com", "p", "n", null, null);

        Mockito.doReturn(true).when(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.doReturn(null).when(roleServiceMocked).parseRoleNamesListToRolesList(Mockito.anyList());

        assertThrows(UserServiceCreationException.class, () -> userService.createCompanyFromRequest(signUpCompanyRequest));

        Mockito.verify(userRepositoryMocked).existsByEmail(Mockito.anyString());
        Mockito.verify(roleServiceMocked, Mockito.never()).parseRoleNamesListToRolesList(Mockito.anyList());
        Mockito.verify(companyRepositoryMocked, Mockito.never()).save(Mockito.any());
    }
}
