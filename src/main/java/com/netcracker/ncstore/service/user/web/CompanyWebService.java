package com.netcracker.ncstore.service.user.web;

import com.netcracker.ncstore.dto.CompanyUpdateDTO;
import com.netcracker.ncstore.dto.request.CompanyDetailedInfoRequest;
import com.netcracker.ncstore.dto.request.CompanyUpdateRequest;
import com.netcracker.ncstore.dto.response.CompanyDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyUpdateResponse;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.service.user.interfaces.IUserBusinessService;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import com.netcracker.ncstore.service.user.interfaces.web.ICompanyWebService;
import com.netcracker.ncstore.util.converter.RolesConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CompanyWebService implements ICompanyWebService {
    private final IUserDataService userDataService;
    private final IUserBusinessService userBusinessService;

    public CompanyWebService(final IUserDataService userDataService,
                             final IUserBusinessService userBusinessService) {
        this.userDataService = userDataService;
        this.userBusinessService = userBusinessService;
    }


    @Override
    public CompanyDetailedInfoResponse getDetailedCompanyInfo(CompanyDetailedInfoRequest request)
            throws GeneralNotFoundException, GeneralPermissionDeniedException {
        try {
            User user = userDataService.getUserByEmail(request.getEmailOfCompany());

            Company company = userDataService.getCompany(user.getId());

            if (!user.getEmail().equals(request.getEmailOfIssuer())) {
                throw new GeneralPermissionDeniedException("Only user himself can view his company detailed info. ");
            }

            return new CompanyDetailedInfoResponse(
                    user.getEmail(),
                    user.getBalance(),
                    EUserType.COMPANY,
                    company.getCompanyName(),
                    company.getDescription(),
                    company.getFoundationDate(),
                    RolesConverter.rolesListToRoleNamesList(user.getRoles())
            );

        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public CompanyInfoResponse getPublicCompanyInfo(UUID companyId) throws GeneralNotFoundException {
        try {
            Company company = userDataService.getCompany(companyId);

            return new CompanyInfoResponse(
                    EUserType.COMPANY,
                    company.getCompanyName(),
                    company.getDescription(),
                    company.getFoundationDate(),
                    RolesConverter.rolesListToRoleNamesList(company.getUser().getRoles())
            );

        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public CompanyUpdateResponse updateCompanyInfo(CompanyUpdateRequest request) {
        try {
            User user = userDataService.getUserByEmail(request.getEmailOfUser());

            CompanyUpdateDTO updateDTO = new CompanyUpdateDTO(
                    user.getId(),
                    request.getCompanyName(),
                    request.getDescription(),
                    request.getFoundationDate()
            );

            Company company = userBusinessService.updateCompanyInfo(updateDTO);

            return new CompanyUpdateResponse(
                    company.getCompanyName(),
                    company.getDescription(),
                    company.getFoundationDate()
            );
        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        } catch (UserServiceValidationException validationException) {
            throw new GeneralBadRequestException(validationException.getMessage(), validationException);
        }
    }
}
