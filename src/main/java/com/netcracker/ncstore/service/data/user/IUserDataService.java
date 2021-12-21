package com.netcracker.ncstore.service.data.user;

import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;

import java.util.UUID;

/**
 * Interface for all services which are used for accessing User info
 */
public interface IUserDataService {
    /**
     * Returns supplier name for provided user
     *
     * @param userId the UUID of user
     * @return supplier name for that user
     * @throws UserServiceNotFoundException when user with such UUID does not exist
     */
    String getSupplierNameByUserId(final UUID userId) throws UserServiceNotFoundException;

    /**
     * Returns public name for provided user
     *
     * @param userId the UUID of user
     * @return public name for that user
     * @throws UserServiceNotFoundException when user with such UUID does not exist
     */
    String getPublicNameForUser(final UUID userId) throws UserServiceNotFoundException;

    /**
     * Returns Company entity based on provided userId
     *
     * @param userId the id of user whose company data is needed
     * @return Company entity behind provided user
     * @throws UserServiceNotFoundException when no Company data found for that user or user does not exist
     */
    Company getCompany(UUID userId) throws UserServiceNotFoundException;

    /**
     * Returns Person entity based on provided userId
     *
     * @param userId the id of user whose person data is needed
     * @return Person entity behind provided user
     * @throws UserServiceNotFoundException when no Person data found for that user or user does not exist
     */
    Person getPerson(UUID userId) throws UserServiceNotFoundException;

    /**
     * Returns User entity by using email.
     *
     * @param email email of user
     * @return User entity behind provided email
     * @throws UserServiceNotFoundException when user with such email does not exist
     */
    User getUserByEmail(String email) throws UserServiceNotFoundException;

    /**
     * Returns User entity by using UUID.
     *
     * @param userId UUID of user
     * @return User entity behind provided UUID
     * @throws UserServiceNotFoundException when user with such UUID does not exist
     */
    User getUserById(UUID userId) throws UserServiceNotFoundException;

    /**
     * Checks whether user is company
     *
     * @param userId UUID of user
     * @return true if is user is company
     */
    boolean isCompany(UUID userId);

    /**
     * Checks whether user is person
     *
     * @param userId UUID of user
     * @return true if is user is person
     */
    boolean isPerson(UUID userId);
}
