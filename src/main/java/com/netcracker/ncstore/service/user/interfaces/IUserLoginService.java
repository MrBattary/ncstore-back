package com.netcracker.ncstore.service.user.interfaces;

import com.netcracker.ncstore.dto.LoginSuccessDTO;
import com.netcracker.ncstore.dto.UserEmailPasswordSignInDTO;
import com.netcracker.ncstore.exception.UserServiceLoginException;

/**
 * Interface for all services that perform user login
 */
public interface IUserLoginService {
    /**
     * Signs in as any registered user using password and email
     *
     * @param request DTO containing email and password
     * @return LoginSuccessDTO containing user entity and token
     * @throws UserServiceLoginException - user does not exist or password is incorrect
     */
    LoginSuccessDTO loginUsingEmailAndPassword(UserEmailPasswordSignInDTO request) throws UserServiceLoginException;
}
