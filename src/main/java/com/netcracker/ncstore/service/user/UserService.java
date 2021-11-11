package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.role.IRoleService;
import com.netcracker.ncstore.util.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {
    private final IRoleService roleService;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;

    private final Logger log;

    public UserService(final IRoleService roleService,
                       final UserRepository userRepository,
                       final PersonRepository personRepository,
                       final CompanyRepository companyRepository) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        log = LoggerFactory.getLogger(UserService.class);
    }

    @Override
    public void checkUserEmail(final String email) throws UserServiceValidationException, UserServiceRepositoryException {
        if (!EmailValidator.isEmailValid(email)) {
            throw new UserServiceValidationException("The email: " + email + " isn't valid");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserServiceRepositoryException("User with email: " + email + " already exists");
        }
    }

    @Override
    public void createPersonFromRequest(SignUpPersonRequest personRequest) throws UserServiceBuildingException {
        try {
            log.info("The building of user with an email: " + personRequest.getEmail() + " begins");
            checkUserEmail(personRequest.getEmail());
            User user = new User(
                    personRequest.getEmail(),
                    personRequest.getPassword(),
                    0,
                    roleService.buildRolesList(personRequest.getRoles())
            );
            personRepository.save(
                    new Person(
                            personRequest.getFirstName(),
                            personRequest.getLastName(),
                            personRequest.getNickName(),
                            personRequest.getBirthday(),
                            user
                    )
            );
            log.info("The building of user with an email: " + personRequest.getEmail() + " completed successfully");
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceBuildingException("Unable to build a user with email: " + personRequest.getEmail(), e);
        }

    }

    @Override
    public void createCompanyFromRequest(SignUpCompanyRequest companyRequest) throws UserServiceBuildingException {
        try {
            log.info("The building of user with an email: " + companyRequest.getEmail() + " begins");
            checkUserEmail(companyRequest.getEmail());
            User user = new User(
                    companyRequest.getEmail(),
                    companyRequest.getPassword(),
                    0,
                    roleService.buildRolesList(companyRequest.getRoles())
            );
            companyRepository.save(
                    new Company(
                            companyRequest.getCompanyName(),
                            null,
                            companyRequest.getFoundationDate(),
                            user
                    )
            );
            log.info("The building of user with an email: " + companyRequest.getEmail() + " completed successfully");
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceBuildingException("Unable to build a user with email: " + companyRequest.getEmail(), e);
        }
    }

    @Override
    public UserTypeEmailPasswordRolesDTO getUserAuthDataByEmail(String email) throws UserServiceRepositoryException {
        Person foundPerson = personRepository.findPersonByUserEmail(email);
        if (foundPerson != null) {
            List<Role> userRolesList = foundPerson.getUser().getRoles();
            List<ERoleName> roleNamesList = new ArrayList<>(userRolesList.size());
            for (Role userRole : userRolesList) {
                roleNamesList.add(userRole.getRoleName());
            }

            return new UserTypeEmailPasswordRolesDTO(
                    EUserType.PERSON,
                    foundPerson.getUser().getEmail(),
                    foundPerson.getUser().getPassword(),
                    roleNamesList
            );
        }

        Company foundCompany = companyRepository.findCompanyByUserEmail(email);
        if (foundCompany != null) {
            List<Role> userRolesList = foundCompany.getUser().getRoles();
            List<ERoleName> roleNamesList = new ArrayList<>(userRolesList.size());
            for (Role userRole : userRolesList) {
                roleNamesList.add(userRole.getRoleName());
            }

            return new UserTypeEmailPasswordRolesDTO(
                    EUserType.COMPANY,
                    foundCompany.getUser().getEmail(),
                    foundCompany.getUser().getPassword(),
                    roleNamesList
            );
        }
        throw new UserServiceRepositoryException("Unable to find a user with email: " + email);
    }
}
