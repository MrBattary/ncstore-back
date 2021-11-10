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
import org.springframework.dao.DataAccessException;
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
            log.info("The sign up process of person with an email: " + personRequest.getEmail() + " begins");
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
            log.info("The sign up process of person with an email: " + personRequest.getEmail() + " completed successfully");
        } catch (UserServiceBuildingException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of person with an email: "
                    + personRequest.getEmail() + " failed", e);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of person with an email: "
                    + personRequest.getEmail() + " unexpectedly failed", e);
        }
    }

    @Override
    public void signUpCompany(final SignUpCompanyRequest companyRequest)
            throws AuthServiceException {
        try {
            log.info("The sign up process of company with an email: " + companyRequest.getEmail() + " begins");
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
                            null,
                            companyRequest.getFoundationDate(),
                            user
                    )
            );
            log.info("The sign up process of company with an email: " + companyRequest.getEmail() + " completed successfully");
        } catch (UserServiceBuildingException | DataAccessException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of company with an email: "
                    + companyRequest.getEmail() + " failed", e);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new AuthServiceException("The sign up process of person with an email: "
                    + companyRequest.getEmail() + " unexpectedly failed", e);
        }
    }
}
