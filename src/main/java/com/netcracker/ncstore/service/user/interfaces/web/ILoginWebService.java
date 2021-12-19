package com.netcracker.ncstore.service.user.interfaces.web;

import com.netcracker.ncstore.dto.request.SignInEmailPasswordRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;

/**
 * Interface for all WEB services related to user login.
 */
public interface ILoginWebService {
    /**
     * Logins user and returns token
     *
     * @param request DTO containing request info
     * @return SignInResponse HTTP response for that request
     */
    SignInResponse signInByEmailAndPassword(SignInEmailPasswordRequest request) throws GeneralBadRequestException;
}
