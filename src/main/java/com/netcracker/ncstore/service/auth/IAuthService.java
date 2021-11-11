package com.netcracker.ncstore.service.auth;

import com.netcracker.ncstore.dto.request.SignInRequest;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;

/**
 * Auth service that handles auth controller
 */
public interface IAuthService {
    /**
     * Person sign up
     *
     * @param request - SignUpPersonRequest
     * @throws AuthServiceException - person already exist
     */
    void signUpPerson(SignUpPersonRequest request) throws AuthServiceException;

    /**
     * Company sign up
     *
     * @param request - SignUpPersonRequest
     * @throws AuthServiceException - company already exist
     */
    void signUpCompany(SignUpCompanyRequest request) throws AuthServiceException;

    /**
     * Sign in as any registered user
     *
     * @param request - login and password
     * @return - SignInResponse with token
     * @throws AuthServiceException - user isn't exist/incorrect password
     */
    SignInResponse signIn(SignInRequest request) throws AuthServiceException;
}
