package com.netcracker.ncstore.service.user.interfaces.web;

import com.netcracker.ncstore.dto.request.UserAddBalanceRequest;
import com.netcracker.ncstore.dto.request.UserBalanceGetRequest;
import com.netcracker.ncstore.dto.request.UserChangePasswordRequest;
import com.netcracker.ncstore.dto.response.UserAddBalanceResponse;
import com.netcracker.ncstore.dto.response.UserBalanceGetResponse;

/**
 * Interface for all WEB services related to basic User entity operations.
 */
public interface IUserBaseWebService {
    /**
     * Adds money to user balance and creates HTTP response to that request
     *
     * @param request DTO containing request info
     * @return UserAddBalanceResponse HTTP response for that request
     */
    UserAddBalanceResponse addMoneyToUserBalance(UserAddBalanceRequest request);

    /**
     * Changes password for user
     */
    void changePasswordForUser(UserChangePasswordRequest request);

    /**
     * Gets user balance and returns it in HTTP response
     *
     * @param request DTO containing email of user whose balance will be returned and email of issuer
     * @return UserBalanceGetResponse HTTP response for that request
     */
    UserBalanceGetResponse getBalanceOfUser(UserBalanceGetRequest request);
}
