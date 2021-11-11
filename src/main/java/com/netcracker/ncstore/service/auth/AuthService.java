package com.netcracker.ncstore.service.auth;

import com.netcracker.ncstore.dto.UserLoginAndRolesDTO;
import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.request.SignInRequest;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.security.IJwtTokenService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.service.user.UserServiceBuildingException;
import com.netcracker.ncstore.service.user.UserServiceRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService implements IAuthService {
    private final IUserService userService;
    private final IJwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final Logger log;

    public AuthService(final IUserService userService,
                       final IJwtTokenService jwtTokenService,
                       final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.log = LoggerFactory.getLogger(AuthService.class);
    }

    @Override
    public void signUpPerson(final SignUpPersonRequest personRequest) throws AuthServiceException {
        try {
            log.info("The sign up process of person with an email: " + personRequest.getEmail() + " begins");
            personRequest.setPassword(passwordEncoder.encode(personRequest.getPassword()));
            userService.createPersonFromRequest(personRequest);
            log.info("The sign up process of person with an email: " + personRequest.getEmail()
                    + " completed successfully");
        } catch (UserServiceBuildingException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of person with an email: "
                    + personRequest.getEmail() + " failed", e);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of person with an email: "
                    + personRequest.getEmail() + " unexpectedly failed", e);
        }
    }

    @Override
    public void signUpCompany(final SignUpCompanyRequest companyRequest) throws AuthServiceException {
        try {
            log.info("The sign up process of company with an email: " + companyRequest.getEmail() + " begins");
            companyRequest.setPassword(passwordEncoder.encode(companyRequest.getPassword()));
            userService.createCompanyFromRequest(companyRequest);
            log.info("The sign up process of company with an email: " + companyRequest.getEmail()
                    + " completed successfully");
        } catch (UserServiceBuildingException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of company with an email: "
                    + companyRequest.getEmail() + " failed", e);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of person with an email: "
                    + companyRequest.getEmail() + " unexpectedly failed", e);
        }
    }

    @Override
    public SignInResponse signIn(final SignInRequest request) throws AuthServiceException {
        try {
            UserTypeEmailPasswordRolesDTO userDTO = userService.getUserAuthDataByEmail(request.getEmail());

            if (!passwordEncoder.matches(request.getPassword(), userDTO.getPassword())) {
                throw new AuthServiceException("The sign in process of user with an email: "
                        + request.getEmail() + " failed due to incorrect password");
            }

            return new SignInResponse(
                    userDTO.getType(),
                    userDTO.getRoles(),
                    jwtTokenService.createToken(
                            new UserLoginAndRolesDTO(
                                    userDTO.getEmail(),
                                    userDTO.getRoles()
                            )
                    )
            );
        } catch (UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign in process of user with an email: "
                    + request.getEmail() + " failed", e);
        }
    }
}
