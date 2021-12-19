package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.RegisterUserDTO;
import com.netcracker.ncstore.dto.create.CompanyRegisterPasswordEmailDTO;
import com.netcracker.ncstore.dto.create.PersonRegisterPasswordEmailDTO;
import com.netcracker.ncstore.exception.UserServiceRegistrationException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.user.interfaces.IUserRegistrationService;
import com.netcracker.ncstore.util.validator.RegisterDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegistrationBusinessService implements IUserRegistrationService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationBusinessService(final UserRepository userRepository,
                                       final PersonRepository personRepository,
                                       final CompanyRepository companyRepository,
                                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public Person registerUserAsPersonUsingEmailAndPassword(PersonRegisterPasswordEmailDTO request)
            throws UserServiceRegistrationException {

        log.info("Start registering new person with email " + request.getEmail());

        User user = registerUserUsingEmailAndPassword(
                new RegisterUserDTO(
                        request.getEmail(),
                        request.getPassword(),
                        request.getRoles()
                )
        );

        Person person = personRepository.save(
                new Person(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getNickName(),
                        request.getBirthday(),
                        user
                )
        );

        log.info("Successfully registered new person with email " + request.getEmail());

        return person;

    }

    @Override
    @Transactional
    public Company registerUserAsCompanyUsingEmailAndPassword(CompanyRegisterPasswordEmailDTO request)
            throws UserServiceRegistrationException {

        log.info("Start registering new company with email " + request.getEmail());

        User user = registerUserUsingEmailAndPassword(
                new RegisterUserDTO(
                        request.getEmail(),
                        request.getPassword(),
                        request.getRoles()
                )
        );

        Company company = companyRepository.save(
                new Company(
                        request.getCompanyName(),
                        request.getDescription(),
                        request.getFoundationDate(),
                        user
                )
        );

        log.info("Successfully registered new company with email " + request.getEmail());

        return company;
    }

    private User registerUserUsingEmailAndPassword(RegisterUserDTO registerUserDTO) {
        try {

            log.info("Adding user with email " + registerUserDTO.getEmail() + " to database");

            if (!RegisterDataValidator.isEmailValid(registerUserDTO.getEmail())) {
                throw new UserServiceRegistrationException("Email " + registerUserDTO.getEmail() + " is not valid. ");
            }
            if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
                throw new UserServiceRegistrationException("Email " + registerUserDTO.getEmail() + " is already taken. ");
            }
            if (RegisterDataValidator.isPasswordValid(registerUserDTO.getPassword())) {
                throw new UserServiceRegistrationException("Password do not meet requirements. " + RegisterDataValidator.getPasswordRequirementsAsText());
            }

            String encodedPassword = passwordEncoder.encode(registerUserDTO.getPassword());

            User user = userRepository.save(
                    new User(
                            registerUserDTO.getEmail(),
                            encodedPassword,
                            registerUserDTO.getRoles()
                    )
            );

            log.info("Successfully added user with email " + registerUserDTO.getEmail() + " to database");

            return user;
        } catch (UserServiceRegistrationException e) {
            log.warn("Unable to add user with email " + registerUserDTO.getEmail() + " to database");
            throw e;
        }
    }
}
