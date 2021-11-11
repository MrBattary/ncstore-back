package com.netcracker.ncstore.service.auth;

import com.netcracker.ncstore.dto.UserEmailAndRolesDTO;
import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.request.SignInRequest;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.security.IJwtTokenService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.service.user.UserServiceCreationException;
import com.netcracker.ncstore.service.user.UserServiceRepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class AuthServiceTest {
    private IAuthService authService;

    @Mock
    private AutoCloseable closeable;

    @Mock
    private IUserService userServiceMocked;

    @Mock
    private IJwtTokenService jwtTokenServiceMocked;

    @Mock
    private PasswordEncoder passwordEncoderMocked;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        authService = new AuthService(userServiceMocked, jwtTokenServiceMocked, passwordEncoderMocked);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void signUpPerson() {
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("e", "p", "n", "f", "l", null, null);

        Mockito.doReturn("pEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doNothing().when(userServiceMocked).createPersonFromRequest(Mockito.any(SignUpPersonRequest.class));

        authService.signUpPerson(signUpPersonRequest);

        Mockito.verify(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.verify(userServiceMocked).createPersonFromRequest(Mockito.any(SignUpPersonRequest.class));
    }

    @Test
    void signUpPersonUserServiceException() {
        SignUpPersonRequest signUpPersonRequest = new SignUpPersonRequest("e", "p", "n", "f", "l", null, null);

        Mockito.doReturn("pEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doThrow(new UserServiceCreationException("Test creation exception"))
                .when(userServiceMocked).createPersonFromRequest(Mockito.any(SignUpPersonRequest.class));

        assertThrows(AuthServiceException.class, () -> authService.signUpPerson(signUpPersonRequest));

        Mockito.verify(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.verify(userServiceMocked).createPersonFromRequest(Mockito.any(SignUpPersonRequest.class));
    }

    @Test
    void signUpCompany() {
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("e", "p", "n", null, null);

        Mockito.doReturn("pEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doNothing().when(userServiceMocked).createCompanyFromRequest(Mockito.any(SignUpCompanyRequest.class));

        authService.signUpCompany(signUpCompanyRequest);

        Mockito.verify(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.verify(userServiceMocked).createCompanyFromRequest(Mockito.any(SignUpCompanyRequest.class));
    }

    @Test
    void signUpCompanyUserServiceException() {
        SignUpCompanyRequest signUpCompanyRequest = new SignUpCompanyRequest("e", "p", "n", null, null);

        Mockito.doReturn("pEncoded").when(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.doThrow(new UserServiceCreationException("Test creation exception"))
                .when(userServiceMocked).createCompanyFromRequest(Mockito.any(SignUpCompanyRequest.class));

        assertThrows(AuthServiceException.class, () -> authService.signUpCompany(signUpCompanyRequest));

        Mockito.verify(passwordEncoderMocked).encode(Mockito.anyString());
        Mockito.verify(userServiceMocked).createCompanyFromRequest(Mockito.any(SignUpCompanyRequest.class));
    }

    @Test
    void signIn() {
        SignInRequest signInRequest = new SignInRequest("email", "p");
        UserTypeEmailPasswordRolesDTO dto = new UserTypeEmailPasswordRolesDTO(EUserType.PERSON, "email", "eEncoded", null);

        Mockito.doReturn(dto).when(userServiceMocked).getUserAuthDataByEmail(Mockito.anyString());
        Mockito.doReturn(true).when(passwordEncoderMocked).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.doReturn("token").when(jwtTokenServiceMocked).createToken(Mockito.any(UserEmailAndRolesDTO.class));

        SignInResponse response = authService.signIn(signInRequest);
        assertEquals(EUserType.PERSON, response.getType());
        assertEquals("token", response.getToken());

        Mockito.verify(userServiceMocked).getUserAuthDataByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoderMocked).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jwtTokenServiceMocked).createToken(Mockito.any(UserEmailAndRolesDTO.class));
    }

    @Test
    void signInUserNotExist() {
        SignInRequest signInRequest = new SignInRequest("email", "p");

        Mockito.doThrow(new UserServiceRepositoryException("Test user repository exception")).when(userServiceMocked).getUserAuthDataByEmail(Mockito.anyString());

        assertThrows(AuthServiceException.class, () -> authService.signIn(signInRequest));

        Mockito.verify(userServiceMocked).getUserAuthDataByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoderMocked, Mockito.never()).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jwtTokenServiceMocked, Mockito.never()).createToken(Mockito.any(UserEmailAndRolesDTO.class));
    }

    @Test
    void signInPasswordIncorrect() {
        SignInRequest signInRequest = new SignInRequest("email", "p");
        UserTypeEmailPasswordRolesDTO dto = new UserTypeEmailPasswordRolesDTO(EUserType.PERSON, "email", "eEncoded", null);

        Mockito.doReturn(dto).when(userServiceMocked).getUserAuthDataByEmail(Mockito.anyString());
        Mockito.doReturn(false).when(passwordEncoderMocked).matches(Mockito.anyString(), Mockito.anyString());

        assertThrows(AuthServiceException.class, () -> authService.signIn(signInRequest));

        Mockito.verify(userServiceMocked).getUserAuthDataByEmail(Mockito.anyString());
        Mockito.verify(passwordEncoderMocked).matches(Mockito.anyString(), Mockito.anyString());
        Mockito.verify(jwtTokenServiceMocked, Mockito.never()).createToken(Mockito.any(UserEmailAndRolesDTO.class));
    }
}
