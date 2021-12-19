package com.netcracker.ncstore.service.user.web;

import com.netcracker.ncstore.dto.request.PersonDetailedInfoRequest;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import com.netcracker.ncstore.service.user.interfaces.web.IPersonWebService;
import com.netcracker.ncstore.util.converter.RolesConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
public class PersonWebService implements IPersonWebService {
    private final IUserDataService userDataService;

    public PersonWebService(IUserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @Override
    public PersonDetailedInfoResponse getDetailedPersonInfo(PersonDetailedInfoRequest request)
            throws GeneralNotFoundException, GeneralPermissionDeniedException {
        try {
            User user = userDataService.getUserByEmail(request.getEmailOfPerson());

            Person person = userDataService.getPerson(user.getId());

            if (!user.getEmail().equals(request.getEmailOfIssuer())) {
                throw new GeneralPermissionDeniedException("Only user himself can view his person detailed info. ");
            }

            return new PersonDetailedInfoResponse(
                    user.getEmail(),
                    user.getBalance(),
                    EUserType.PERSON,
                    person.getNickName(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getBirthday(),
                    RolesConverter.rolesListToRoleNamesList(user.getRoles())
            );

        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public PersonInfoResponse getPublicPersonInfo(UUID personId) throws GeneralNotFoundException {
        try {
            Person person = userDataService.getPerson(personId);
            User user = person.getUser();

            String firstName, lastName;
            LocalDate birthday;

            //Made for security reason because giving personal data of regular user is illegal
            boolean isSupplier = user.getRoles().stream().anyMatch(e -> e.getRoleName().equals(ERoleName.SUPPLIER));
            if (isSupplier) {
                firstName = person.getFirstName();
                lastName = person.getLastName();
                birthday = person.getBirthday();
            } else {
                firstName = "";
                lastName = "";
                birthday = LocalDate.MIN;
            }

            return new PersonInfoResponse(
                    EUserType.PERSON,
                    person.getNickName(),
                    firstName,
                    lastName,
                    birthday,
                    RolesConverter.rolesListToRoleNamesList(user.getRoles())
            );
        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }
}
