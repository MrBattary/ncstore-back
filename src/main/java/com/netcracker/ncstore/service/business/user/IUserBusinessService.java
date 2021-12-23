package com.netcracker.ncstore.service.business.user;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.CompanyUpdateDTO;
import com.netcracker.ncstore.dto.PersonUpdateDTO;
import com.netcracker.ncstore.exception.UserServiceBalancePaymentException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServicePasswordChangingException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;

public interface IUserBusinessService {
    /**
     * Adds money to user balance using payment system and card info
     *
     * @param addBalanceDTO DTO containing payment info
     * @return new balance amount
     * @throws UserServiceBalancePaymentException when it is impossible to add money using provided data
     */
    double addMoneyToUserBalance(AddBalanceDTO addBalanceDTO) throws UserServiceBalancePaymentException, UserServiceNotFoundException;

    /**
     * Changes user password
     *
     * @param changePasswordDTO DTO containing email of user plus old and new passwords
     * @throws UserServicePasswordChangingException when can not change password
     */
    void changeUserPassword(ChangePasswordDTO changePasswordDTO) throws UserServicePasswordChangingException;

    /**
     * Changes roles for user
     *
     * @param user user
     * @param role new roles
     * @return User with changed roles
     * @throws UserServiceValidationException when roles can not be added to user
     */
    User addRoleToUser(User user, Role role) throws UserServiceValidationException;

    /**
     * Updates person info for user
     *
     * @param dto DTO containing update info
     * @return updated Person entity
     * @throws UserServiceNotFoundException when user is not person or not found
     * @throws UserServiceValidationException when provided data is invalid
     */
    Person updatePersonInfo(PersonUpdateDTO dto) throws UserServiceNotFoundException, UserServiceValidationException;

    /**
     * Updates company info for user
     *
     * @param dto DTO containing update info
     * @return updated Company entity
     * @throws UserServiceNotFoundException when user is not person or not found
     * @throws UserServiceValidationException when provided data is invalid
     */
    Company updateCompanyInfo(CompanyUpdateDTO dto) throws UserServiceNotFoundException, UserServiceValidationException;
}
