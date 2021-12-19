package com.netcracker.ncstore.service.user.web;

import com.netcracker.ncstore.dto.create.CompanyRegisterPasswordEmailDTO;
import com.netcracker.ncstore.dto.create.PersonRegisterPasswordEmailDTO;
import com.netcracker.ncstore.dto.request.RegisterCompanyRequest;
import com.netcracker.ncstore.dto.request.RegisterPersonRequest;
import com.netcracker.ncstore.exception.RoleServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServiceRegistrationException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.service.role.interfaces.IRoleDataService;
import com.netcracker.ncstore.service.user.interfaces.IUserRegistrationService;
import com.netcracker.ncstore.service.user.interfaces.web.IRegistrationWebService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistrationWebService implements IRegistrationWebService {
    private final IUserRegistrationService registrationService;
    private final IRoleDataService roleService;

    public RegistrationWebService(final IUserRegistrationService registrationService,
                                  final IRoleDataService roleService) {
        this.registrationService = registrationService;
        this.roleService = roleService;
    }


    @Override
    public void registerPerson(RegisterPersonRequest request) throws GeneralBadRequestException {
        try {

            List<Role> roleList = roleNamesToRoles(request.getRoles());

            PersonRegisterPasswordEmailDTO dto = new PersonRegisterPasswordEmailDTO(
                    request.getEmail(),
                    request.getPassword(),
                    request.getNickName(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getBirthday(),
                    roleList
            );

            registrationService.registerUserAsPersonUsingEmailAndPassword(dto);

        } catch (UserServiceRegistrationException
                | RoleServiceNotFoundException exception) {

            throw new GeneralBadRequestException(exception.getMessage(), exception);
        }
    }

    @Override
    public void registerCompany(RegisterCompanyRequest request) throws GeneralBadRequestException {
        try {
            List<Role> roleList = roleNamesToRoles(request.getRoles());

            CompanyRegisterPasswordEmailDTO dto = new CompanyRegisterPasswordEmailDTO(
                    request.getEmail(),
                    request.getPassword(),
                    request.getCompanyName(),
                    "User did not provide a description. ",
                    request.getFoundationDate(),
                    roleList
            );

            registrationService.registerUserAsCompanyUsingEmailAndPassword(dto);
        } catch (UserServiceRegistrationException
                | RoleServiceNotFoundException exception) {

            throw new GeneralBadRequestException(exception.getMessage(), exception);
        }

    }

    private List<Role> roleNamesToRoles(List<String> roleNames) throws RoleServiceNotFoundException {
        return roleNames.
                stream().
                map(roleService::getRoleByName).
                collect(Collectors.toList());
    }
}
