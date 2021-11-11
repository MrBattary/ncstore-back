package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;

public interface IUserService {
    /**
     * Check user email
     *
     * @param email - email
     * @throws UserServiceValidationException - if email is incorrect
     * @throws UserServiceRepositoryException - if email is exist in the DB
     */
    void checkNewUserEmail(String email) throws UserServiceValidationException, UserServiceRepositoryException;

    /**
     * Create Person from SignUpPersonRequest
     *
     * @param request - SignUpPersonRequest
     * @throws UserServiceBuildingException - if not created
     */
    void createPersonFromRequest(SignUpPersonRequest request) throws UserServiceBuildingException;

    /**
     * Create Person from SignUpCompanyRequest
     *
     * @param request - SignUpCompanyRequest
     * @throws UserServiceBuildingException - if not created
     */
    void createCompanyFromRequest(SignUpCompanyRequest request) throws UserServiceBuildingException;

    /**
     * Returns data for authentication (type, email, password, roles)
     *
     * @param email - email
     * @return - UserTypeEmailPasswordRolesDTO
     * @throws UserServiceRepositoryException - if user not found
     */
    UserTypeEmailPasswordRolesDTO getUserAuthDataByEmail(String email) throws UserServiceRepositoryException;
}
