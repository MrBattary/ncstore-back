package com.netcracker.ncstore.service.auth;

import com.netcracker.ncstore.dto.UserModelWithoutIdDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.service.user.UserServiceBuildingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class AuthService implements IAuthService {
    private final IUserService userService;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final Logger log;

    public AuthService(final IUserService userService,
                       final PersonRepository personRepository,
                       final CompanyRepository companyRepository) {
        this.userService = userService;
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.log = LoggerFactory.getLogger(AuthService.class);
    }

    @Override
    public void signUpPerson(final SignUpPersonRequest personRequest)
            throws AuthServiceException {
        try {
            log.info("The sign up person process with an email: " + personRequest.getEmail() + " begins");
            User user = userService.buildUserFromUserModelDTO(
                    new UserModelWithoutIdDTO(
                            personRequest.getEmail(),
                            personRequest.getPassword(),
                            0,
                            personRequest.getRoles()
                    )
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
            log.info("The sign up person process with an email: " + personRequest.getEmail() + " completed successfully");
        } catch (UserServiceBuildingException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("User build with an email: " + personRequest.getEmail() + " failed", e);
        }
    }

    @Override
    public void signUpCompany(final SignUpCompanyRequest companyRequest)
            throws AuthServiceException {
        try {
            log.info("The sign up company process with an email: " + companyRequest.getEmail() + " begins");
            User user = userService.buildUserFromUserModelDTO(
                    new UserModelWithoutIdDTO(
                            companyRequest.getEmail(),
                            companyRequest.getPassword(),
                            0,
                            companyRequest.getRoles()
                    )
            );
            companyRepository.save(
                    new Company(
                            companyRequest.getCompanyName(),
                            "",
                            companyRequest.getFoundationDate(),
                            user
                    )
            );
            log.info("The sign up company process with an email: " + companyRequest.getEmail() + " completed successfully");
        } catch (UserServiceBuildingException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("User build with an email: " + companyRequest.getEmail() + " failed", e);
        }
    }
}
