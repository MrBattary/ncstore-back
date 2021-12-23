package com.netcracker.ncstore.service.business.register;

import com.netcracker.ncstore.dto.create.CompanyRegisterPasswordEmailDTO;
import com.netcracker.ncstore.dto.create.PersonRegisterPasswordEmailDTO;
import com.netcracker.ncstore.exception.UserServiceRegistrationException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;

public interface IUserRegistrationService {
    /**
     * Creates new user as person using email and password
     *
     * @param request - SignUpPersonRequest
     * @throws UserServiceRegistrationException if not registered
     */
    Person registerUserAsPersonUsingEmailAndPassword(PersonRegisterPasswordEmailDTO request) throws UserServiceRegistrationException;

    /**
     * Creates new user as company using email and password
     *
     * @param request - SignUpCompanyRequest
     * @throws UserServiceRegistrationException if not registered
     */
    Company registerUserAsCompanyUsingEmailAndPassword(CompanyRegisterPasswordEmailDTO request) throws UserServiceRegistrationException;
}
