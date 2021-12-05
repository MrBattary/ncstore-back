package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.data.CompanyDTO;
import com.netcracker.ncstore.dto.data.PersonDTO;
import com.netcracker.ncstore.dto.data.UserDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.CompanyDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyInfoResponse;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.exception.UserServiceCreationException;
import com.netcracker.ncstore.exception.UserServiceRepositoryException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.model.User;

import java.util.UUID;

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
     * Adds provided amount of money to balance
     *
     * @param addBalanceDTO - DTO containing email and amount to be added
     * @return new balance value
     */
    double addMoneyToBalance(AddBalanceDTO addBalanceDTO);

    /**
     * Changes user password
     *
     * @param changePasswordDTO DTO containing email of user plus old and new passwords
     */
    void changeUserPassword(ChangePasswordDTO changePasswordDTO);

    /**
     * Returns email of user with provided UUID
     *
     * @param id - UUID of user
     * @return email of user
     */
    String getUserEmailById(UUID id);

    /**
     * Returns UserDTO instance by using email
     *
     * @param email - email of user
     * @return UserDTO - DTO containing all info about user entity
     */
    UserDTO loadUserByEmail(String email);

    /**
     * Returns User entity by using email.
     * Should be used only when real entity is needed.
     *
     * @param email - email of user
     * @return User - real User entity
     */
    User loadUserEntityByEmail(String email);

    /**
     * Returns User entity by using UUID.
     * Should be used only when real entity is needed.
     *
     * @param id - UUID pf user
     * @return User - real User entity
     */
    User loadUserEntityById(UUID id);

    /**
     * Returns Company entity data based on provided UserId
     * Returns null if there is no company info for that user
     *
     * @param userId the id of user whose company data is needed
     * @return CompanyDTO or null if no data for that user
     */
    CompanyDTO getCompanyData(UUID userId);

    /**
     * Returns Person entity data based on provided UserId
     * Returns null if there is no person info for that user
     *
     * @param userId the id of user whose company data is needed
     * @return PersonDTO or null if no data for that user
     */
    PersonDTO getPersonData(UUID userId);

    /**
     * Returns detailed information about Company of principal.
     * Returns information only if principal requests self's company info.
     *
     * @param email principal of user willing to get info about one's company
     * @return CompanyDetailedInfoResponse
     */
    CompanyDetailedInfoResponse getDetailedCompanyInfo(String email);

    /**
     * Returns information about Company of user with provided ID
     *
     * @return CompanyInfoResponse
     */
    CompanyInfoResponse getBasisCompanyInfo(UUID userId);

    /**
     * Returns detailed information about Person of principal.
     * Returns information only if principal requests personal info.
     *
     * @param email principal of user willing to get personal info
     * @return PersonDetailedInfoResponse
     */
    PersonDetailedInfoResponse getDetailedPersonInfo(String email);

    /**
     * Returns information about Person of user with provided ID
     *
     * @return PersonInfoResponse
     */
    PersonInfoResponse getBasisPersonInfo(UUID userId);


}
