package com.netcracker.ncstore.service.user.interfaces;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.exception.UserServiceBalancePaymentException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServicePasswordChangingException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
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
     * @param user  user
     * @param role new roles
     * @return User with changed roles
     * @throws UserServiceValidationException when roles can not be added to user
     */
    User addRoleToUser(User user, Role role) throws UserServiceValidationException;
}
