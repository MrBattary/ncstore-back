package com.netcracker.ncstore.service.web.person;

import com.netcracker.ncstore.dto.request.PersonDetailedInfoRequest;
import com.netcracker.ncstore.dto.request.PersonUpdateRequest;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.dto.response.PersonUpdateResponse;

import java.util.UUID;

/**
 * Interface for all WEB services related to Person entity operations.
 */
public interface IPersonWebService {
    /**
     * Returns detailed person info for user. Only returns that info to user himself.
     *
     * @param request DTO containing request info
     * @return PersonDetailedInfoResponse
     */
    PersonDetailedInfoResponse getDetailedPersonInfo(PersonDetailedInfoRequest request);

    /**
     * Returns person info for user. Info is public and available for every user.
     *
     * @param personId UUID of user
     * @return PersonInfoResponse
     */
    PersonInfoResponse getPublicPersonInfo(UUID personId);

    /**
     * Updates person info for user. Only available for persons.
     *
     * @param request DTO containing request info
     * @return PersonUpdateResponse
     */
    PersonUpdateResponse updatePersonInfo(PersonUpdateRequest request);
}
