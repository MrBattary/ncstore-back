/*
package com.netcracker.ncstore.service.auth;

import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.service.user.UserServiceBuildingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class AuthServiceTest {
    private IAuthService authService;

    @Mock
    private AutoCloseable closeable;

    @Mock
    private IUserService userServiceMocked;

    @Mock
    private PersonRepository personRepositoryMocked;

    @Mock
    private CompanyRepository companyRepositoryMocked;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        authService = new AuthService(userServiceMocked, personRepositoryMocked, companyRepositoryMocked);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void signUpPerson() {
        User userMocked = Mockito.mock(User.class);
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("e", "p", "n", "f", "l", null, null);

        Mockito.doReturn(userMocked).when(userServiceMocked).buildUserFromUserModelDTO(Mockito.any(UserModelWithoutIdDTO.class));
        Mockito.when(personRepositoryMocked.save(Mockito.any(Person.class))).thenAnswer(i -> i.getArguments()[0]);

        authService.signUpPerson(signUpPersonRequest);
        Mockito.verify(userServiceMocked, Mockito.times(1)).buildUserFromUserModelDTO(Mockito.any());
        Mockito.verify(personRepositoryMocked, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void signUpPersonUserServiceException() {
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("e", "p", "n", "f", "l", null, null);

        Mockito.doThrow(new UserServiceBuildingException("Test building exception"))
                .when(userServiceMocked).buildUserFromUserModelDTO(Mockito.any(UserModelWithoutIdDTO.class));

        assertThrows(AuthServiceException.class, () -> authService.signUpPerson(signUpPersonRequest));
        Mockito.verify(userServiceMocked, Mockito.times(1)).buildUserFromUserModelDTO(Mockito.any());
        Mockito.verify(personRepositoryMocked, Mockito.never()).save(Mockito.any());
    }

    @Test
    void signUpPersonPersonRepositoryException() {
        User userMocked = Mockito.mock(User.class);
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("e", "p", "n", "f", "l", null, null);

        Mockito.doReturn(userMocked).when(userServiceMocked).buildUserFromUserModelDTO(Mockito.any(UserModelWithoutIdDTO.class));
        Mockito.doThrow(new RuntimeException("Test unexpected exception")).when(personRepositoryMocked).save(Mockito.any(Person.class));

        assertThrows(AuthServiceException.class, () -> authService.signUpPerson(signUpPersonRequest));
        Mockito.verify(userServiceMocked, Mockito.times(1)).buildUserFromUserModelDTO(Mockito.any());
        Mockito.verify(personRepositoryMocked, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void signUpCompany() {
        User userMocked = Mockito.mock(User.class);
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("e", "p", "n", null, null);

        Mockito.doReturn(userMocked).when(userServiceMocked).buildUserFromUserModelDTO(Mockito.any(UserModelWithoutIdDTO.class));
        Mockito.when(companyRepositoryMocked.save(Mockito.any(Company.class))).thenAnswer(i -> i.getArguments()[0]);

        authService.signUpCompany(signUpCompanyRequest);
        Mockito.verify(userServiceMocked, Mockito.times(1)).buildUserFromUserModelDTO(Mockito.any());
        Mockito.verify(companyRepositoryMocked, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void signUpCompanyUserServiceException() {
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("e", "p", "n", null, null);

        Mockito.doThrow(new UserServiceBuildingException("Test building exception"))
                .when(userServiceMocked).buildUserFromUserModelDTO(Mockito.any(UserModelWithoutIdDTO.class));

        assertThrows(AuthServiceException.class, () -> authService.signUpCompany(signUpCompanyRequest));
        Mockito.verify(userServiceMocked, Mockito.times(1)).buildUserFromUserModelDTO(Mockito.any());
        Mockito.verify(companyRepositoryMocked, Mockito.never()).save(Mockito.any());
    }

    @Test
    void signUpCompanyCompanyRepositoryException() {
        User userMocked = Mockito.mock(User.class);
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("e", "p", "n", null, null);

        Mockito.doReturn(userMocked).when(userServiceMocked).buildUserFromUserModelDTO(Mockito.any(UserModelWithoutIdDTO.class));
        Mockito.doThrow(new RuntimeException("Test unexpected exception")).when(companyRepositoryMocked).save(Mockito.any(Company.class));

        assertThrows(AuthServiceException.class, () -> authService.signUpCompany(signUpCompanyRequest));
        Mockito.verify(userServiceMocked, Mockito.times(1)).buildUserFromUserModelDTO(Mockito.any());
        Mockito.verify(companyRepositoryMocked, Mockito.times(1)).save(Mockito.any());
    }
}*/
