package com.netcracker.ncstore.service.auth;

import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;

/**
 * Auth service that handles auth controller
 */
public interface IAuthService {
    /**
     * Person sign up
     * @param request - SignUpPersonRequest
     * @throws AuthServiceRepositoryException - person already exist
     * @throws AuthServiceValidationException - invalid data
     */
    void signUpPerson(SignUpPersonRequest request)
            throws AuthServiceRepositoryException, AuthServiceValidationException;

    /**
     * Company sign up
     * @param request - SignUpPersonRequest
     * @throws AuthServiceRepositoryException - company already exist
     * @throws AuthServiceValidationException - invalid data
     */
    void signUpCompany(SignUpCompanyRequest request)
            throws AuthServiceRepositoryException, AuthServiceValidationException;
}
