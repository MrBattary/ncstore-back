package com.netcracker.ncstore.service.web.register;

import com.netcracker.ncstore.dto.request.RegisterCompanyRequest;
import com.netcracker.ncstore.dto.request.RegisterPersonRequest;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;

/**
 * Interface for all WEB services related to registration.
 */
public interface IRegistrationWebService {

    /**
     * Registers person using email, password and additional data
     *
     * @param request DTO containing register data
     * @throws GeneralBadRequestException when register data invalid
     */
    void registerPerson(RegisterPersonRequest request) throws GeneralBadRequestException;

    /**
     * Registers company using email, password and additional data
     *
     * @param request DTO containing register data
     * @throws GeneralBadRequestException when register data invalid
     */
    void registerCompany(RegisterCompanyRequest request) throws GeneralBadRequestException;

}
