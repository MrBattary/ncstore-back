package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.data.UserDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.exception.UserServiceCreationException;
import com.netcracker.ncstore.exception.UserServiceRepositoryException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.model.User;

import java.security.Principal;

/**
 * Interacts with repositories of users, persons and companies
 * Responsible for operations with these entities
 */
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
     * @throws UserServiceCreationException - if not created
     */
    void createPersonFromRequest(SignUpPersonRequest request) throws UserServiceCreationException;

    /**
     * Create Person from SignUpCompanyRequest
     *
     * @param request - SignUpCompanyRequest
     * @throws UserServiceCreationException - if not created
     */
    void createCompanyFromRequest(SignUpCompanyRequest request) throws UserServiceCreationException;

    /**
     * Returns data for authentication (type, email, password, roles)
     *
     * @param email - email
     * @return - UserTypeEmailPasswordRolesDTO
     * @throws UserServiceRepositoryException - if user not found
     */
    UserTypeEmailPasswordRolesDTO getUserAuthDataByEmail(String email) throws UserServiceRepositoryException;

    /**
     * Returns UserDTO instance behind provided Principal
     *
     * @param principal - Principal
     */
    UserDTO loadUserByPrincipal(Principal principal);

    /**
     * Returns User entity behind provided Principal.
     * Should be used only when real entity is needed.
     *
     * @param principal - Principal
     * @return User - real User entity
     */
    User loadUserEntityByPrincipal(Principal principal);
}
