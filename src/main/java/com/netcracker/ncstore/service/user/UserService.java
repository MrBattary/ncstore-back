package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.role.IRoleService;
import com.netcracker.ncstore.util.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public void checkNewUserEmail(final String email) throws UserServiceValidationException, UserServiceRepositoryException {
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
            log.info("The building of person with an email: " + personRequest.getEmail() + " begins");
            checkNewUserEmail(personRequest.getEmail());
            personRepository.save(
                    new Person(
                            personRequest.getFirstName(),
                            personRequest.getLastName(),
                            personRequest.getNickName(),
                            personRequest.getBirthday(),
                            new User(
                                    personRequest.getEmail(),
                                    personRequest.getPassword(),
                                    0,
                                    roleService.parseRoleNamesListToRolesList(personRequest.getRoles())
                            )
                    )
            );
            log.info("The building of person with an email: " + personRequest.getEmail() + " completed successfully");
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceBuildingException("Unable to build a person with email: " + personRequest.getEmail(), e);
        }

    }

    @Override
    public void createCompanyFromRequest(SignUpCompanyRequest companyRequest) throws UserServiceBuildingException {
        try {
            log.info("The building of company with an email: " + companyRequest.getEmail() + " begins");
            checkNewUserEmail(companyRequest.getEmail());
            companyRepository.save(
                    new Company(
                            companyRequest.getCompanyName(),
                            null,
                            companyRequest.getFoundationDate(),
                            new User(
                                    companyRequest.getEmail(),
                                    companyRequest.getPassword(),
                                    0,
                                    roleService.parseRoleNamesListToRolesList(companyRequest.getRoles())
                            )
                    )
            );
            log.info("The building of company with an email: " + companyRequest.getEmail() + " completed successfully");
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceBuildingException("Unable to build a company with email: " + companyRequest.getEmail(), e);
        }
    }

    @Override
    public UserTypeEmailPasswordRolesDTO getUserAuthDataByEmail(String email) throws UserServiceRepositoryException {
        Person foundPerson = personRepository.findPersonByUserEmail(email);
        if (foundPerson != null) {
            return new UserTypeEmailPasswordRolesDTO(
                    EUserType.PERSON,
                    foundPerson.getUser().getEmail(),
                    foundPerson.getUser().getPassword(),
                    roleService.rolesListToRoleNamesList(foundPerson.getUser().getRoles())
            );
        }

        Company foundCompany = companyRepository.findCompanyByUserEmail(email);
        if (foundCompany != null) {
            return new UserTypeEmailPasswordRolesDTO(
                    EUserType.COMPANY,
                    foundCompany.getUser().getEmail(),
                    foundCompany.getUser().getPassword(),
                    roleService.rolesListToRoleNamesList(foundCompany.getUser().getRoles())
            );
        }
        throw new UserServiceRepositoryException("Unable to find a user with email: " + email);
    }
}
