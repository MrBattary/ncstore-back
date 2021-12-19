package com.netcracker.ncstore.service.user.interfaces.web;

import com.netcracker.ncstore.dto.request.PersonDetailedInfoRequest;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;

import java.util.UUID;

/**
 * Interface for all WEB services related to Person entity operations.
 */
public interface IPersonWebService {
    PersonDetailedInfoResponse getDetailedPersonInfo(PersonDetailedInfoRequest request);
    PersonInfoResponse getPublicPersonInfo(UUID personId);
}
