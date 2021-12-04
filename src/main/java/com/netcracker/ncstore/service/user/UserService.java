package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.UserTypeEmailPasswordRolesDTO;
import com.netcracker.ncstore.dto.data.CompanyDTO;
import com.netcracker.ncstore.dto.data.PersonDTO;
import com.netcracker.ncstore.dto.data.UserDTO;
import com.netcracker.ncstore.dto.request.SignUpCompanyRequest;
import com.netcracker.ncstore.dto.request.SignUpPersonRequest;
import com.netcracker.ncstore.dto.response.CompanyDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.CompanyInfoResponse;
import com.netcracker.ncstore.dto.response.PersonDetailedInfoResponse;
import com.netcracker.ncstore.dto.response.PersonInfoResponse;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.exception.UserServiceChangePasswordException;
import com.netcracker.ncstore.exception.UserServiceCreationException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServiceRepositoryException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.repository.CompanyRepository;
import com.netcracker.ncstore.repository.PersonRepository;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.payment.IPaymentService;
import com.netcracker.ncstore.service.role.IRoleService;
import com.netcracker.ncstore.util.converter.RolesConverter;
import com.netcracker.ncstore.util.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    private final IRoleService roleService;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final IPaymentService paymentService;

    private final Logger log;

    public UserService(final IRoleService roleService,
                       final UserRepository userRepository,
                       final PersonRepository personRepository,
                       final CompanyRepository companyRepository,
                       final PasswordEncoder passwordEncoder,
                       final IPaymentService paymentService) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.paymentService = paymentService;
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
    public void createPersonFromRequest(SignUpPersonRequest personRequest) throws UserServiceCreationException {
        try {
            log.info("The creation process of person with an email: " + personRequest.getEmail() + " begins");
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
            log.info("The creation process of person with an email: " + personRequest.getEmail() + " completed successfully");
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceCreationException("Unable to create a person with email: " + personRequest.getEmail(), e);
        }

    }

    @Override
    public void createCompanyFromRequest(SignUpCompanyRequest companyRequest) throws UserServiceCreationException {
        try {
            log.info("The creation process of company with an email: " + companyRequest.getEmail() + " begins");
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
            log.info("The creation process of company with an email: " + companyRequest.getEmail() + " completed successfully");
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceCreationException("Unable to create a company with email: " + companyRequest.getEmail(), e);
        }
    }

    @Override
    public UserTypeEmailPasswordRolesDTO getUserAuthDataByEmail(String email) throws UserServiceRepositoryException {
        Person foundPerson = personRepository.findPersonByUserEmail(email);
        if (foundPerson != null) {
            return new UserTypeEmailPasswordRolesDTO(
                    foundPerson.getUserId(),
                    EUserType.PERSON,
                    foundPerson.getUser().getEmail(),
                    foundPerson.getUser().getPassword(),
                    roleService.rolesListToRoleNamesList(foundPerson.getUser().getRoles())
            );
        }

        Company foundCompany = companyRepository.findCompanyByUserEmail(email);
        if (foundCompany != null) {
            return new UserTypeEmailPasswordRolesDTO(
                    foundCompany.getUserId(),
                    EUserType.COMPANY,
                    foundCompany.getUser().getEmail(),
                    foundCompany.getUser().getPassword(),
                    roleService.rolesListToRoleNamesList(foundCompany.getUser().getRoles())
            );
        }
        throw new UserServiceRepositoryException("Unable to find a user with email: " + email);
    }

    @Override
    public double addMoneyToUserBalance(AddBalanceDTO addBalanceDTO) {
        User user = loadUserEntityByEmail(addBalanceDTO.getEmail());
        log.info("Starting balance payment procedure for user with UUID " + user.getId() + " for amount of " + addBalanceDTO.getAmountToAdd());

        PaymentProceedDTO paymentProceedDTO = new PaymentProceedDTO(
                BigDecimal.valueOf(addBalanceDTO.getAmountToAdd()),
                addBalanceDTO.getNonce()
        );

        String transactionId;

        try {
            transactionId = paymentService.proceedPayment(paymentProceedDTO);
        } catch (PaymentServiceException e) {
            log.error("Can not add money to balance of user with UUID " + user.getId() + " sue to unsuccessful payment");
            throw new PaymentServiceException("Unable to add money to balance due to unsuccessful payment", e);
        }

        user.setBalance(user.getBalance() + addBalanceDTO.getAmountToAdd());
        userRepository.flush();

        log.info("Successfully finished balance payment procedure for user with UUID " + user.getId() + " for amount of " + addBalanceDTO.getAmountToAdd());
        return user.getBalance();
    }

    @Override
    public void changeUserPassword(ChangePasswordDTO changePasswordDTO) {
        User user = loadUserEntityByEmail(changePasswordDTO.getEmail());
        log.info("Changing password for user with UUID " + user.getId());
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            log.error("User with UUID" + user.getId() + " tried to change password, but old password was incorrect");
            throw new UserServiceChangePasswordException("Old password incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.flush();
        log.info("Successfully changed password for user with UUID " + user.getId());
    }

    @Override
    public String getUserEmailById(UUID id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        } else {
            return user.getEmail();
        }
    }


    @Override
    public UserDTO loadUserByEmail(String email) {
        return new UserDTO(loadUserEntityByEmail(email));
    }

    @Override
    public User loadUserEntityByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User loadUserEntityById(UUID id) {
        return userRepository.getById(id);
    }

    @Override
    public CompanyDTO getCompanyData(UUID userId) {
        Company company = companyRepository.findById(userId).orElse(null);

        if (company == null) {
            return null;
        } else {
            return new CompanyDTO(company);
        }
    }

    @Override
    public PersonDTO getPersonData(UUID userId) {
        Person person = personRepository.findById(userId).orElse(null);

        if (person == null) {
            return null;
        } else {
            return new PersonDTO(person);
        }
    }

    @Override
    public CompanyDetailedInfoResponse getDetailedCompanyInfo(String email) {
        User user = loadUserEntityByEmail(email);

        CompanyDTO companyData = getCompanyData(user.getId());
        if (companyData == null) {
            throw new UserServiceNotFoundException("User requested information about company while not being a company.");
        }

        return new CompanyDetailedInfoResponse(
                user.getEmail(),
                user.getBalance(),
                EUserType.COMPANY,
                companyData.getCompanyName(),
                companyData.getDescription(),
                companyData.getFoundationDate(),
                RolesConverter.rolesListToRoleNamesList(user.getRoles()));
    }

    @Override
    public CompanyInfoResponse getBasisCompanyInfo(UUID userId) {
        CompanyDTO companyDTO = getCompanyData(userId);
        User user = userRepository.findById(userId).orElse(null);
        if (companyDTO == null || user == null) {
            throw new UserServiceNotFoundException("No Company info found for provided user UUID " + userId);
        }

        return new CompanyInfoResponse(EUserType.COMPANY,
                companyDTO.getCompanyName(),
                companyDTO.getDescription(),
                companyDTO.getFoundationDate(),
                RolesConverter.rolesListToRoleNamesList(user.getRoles()));
    }

    @Override
    public PersonDetailedInfoResponse getDetailedPersonInfo(String email) {
        User user = loadUserEntityByEmail(email);

        PersonDTO personData = getPersonData(user.getId());
        if (personData == null) {
            throw new UserServiceNotFoundException("User requested information about person while not being a person.");
        }

        return new PersonDetailedInfoResponse(
                user.getEmail(),
                user.getBalance(),
                EUserType.PERSON,
                personData.getNickName(),
                personData.getFirstName(),
                personData.getLastName(),
                personData.getBirthday(),
                RolesConverter.rolesListToRoleNamesList(user.getRoles()));
    }

    @Override
    public PersonInfoResponse getBasisPersonInfo(UUID userId) {
        PersonDTO personData = getPersonData(userId);
        User user = userRepository.findById(userId).orElse(null);
        if (personData == null || user == null) {
            throw new UserServiceNotFoundException("No Person info found for provided user UUID " + userId);
        }

        String firstName, lastName;
        LocalDate birthday;

        //Made for security reason because giving personal data of regular user is illegal
        boolean isSupplier = user.getRoles().stream().anyMatch(e -> e.getRoleName().equals(ERoleName.SUPPLIER));
        if (isSupplier) {
            firstName = personData.getFirstName();
            lastName = personData.getLastName();
            birthday = personData.getBirthday();
        } else {
            firstName = "";
            lastName = "";
            birthday = LocalDate.MIN;
        }

        return new PersonInfoResponse(
                EUserType.PERSON,
                personData.getNickName(),
                firstName,
                lastName,
                birthday,
                RolesConverter.rolesListToRoleNamesList(user.getRoles()));
    }
}
