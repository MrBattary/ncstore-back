package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.UserModelWithoutIdDTO;
import com.netcracker.ncstore.model.User;

public interface IUserService {
    /**
     * Check user email
     *
     * @param email - email
     * @throws UserServiceValidationException - if email is incorrect
     * @throws UserServiceRepositoryException - if email is exist in the DB
     */
    void checkUserEmail(String email) throws UserServiceValidationException, UserServiceRepositoryException;

    /**
     * Build User model from SignUpPersonRequest
     *
     * @param modelDTO - SignUpPersonRequest
     * @return - User model object
     * @throws UserServiceBuildingException - if building is impossible
     */
    User buildUserFromUserModelDTO(UserModelWithoutIdDTO modelDTO)
            throws UserServiceBuildingException;
}
