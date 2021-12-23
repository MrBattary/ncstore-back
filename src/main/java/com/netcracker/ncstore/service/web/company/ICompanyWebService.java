package com.netcracker.ncstore.service.web.company;

import com.netcracker.ncstore.dto.request.CompanyDetailedInfoRequest;
import com.netcracker.ncstore.dto.request.CompanyUpdateRequest;
import com.netcracker.ncstore.dto.response.CompanyDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyUpdateResponse;

import java.util.UUID;

/**
 * Interface for all WEB services related to Company entity operations.
 */
public interface ICompanyWebService {
    CompanyDetailedInfoResponse getDetailedCompanyInfo(CompanyDetailedInfoRequest request);

    CompanyInfoResponse getPublicCompanyInfo(UUID companyId);

    CompanyUpdateResponse updateCompanyInfo(CompanyUpdateRequest request);
}
