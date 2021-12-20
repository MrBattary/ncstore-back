package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.CompanyUpdateDTO;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.PersonUpdateDTO;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.exception.UserServiceBalancePaymentException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServicePasswordChangingException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.model.Company;
import com.netcracker.ncstore.model.Person;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.payment.interfaces.IPaymentService;
import com.netcracker.ncstore.service.user.interfaces.IUserBusinessService;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

@Service
@Slf4j
public class UserBusinessService implements IUserBusinessService {
    private final IUserDataService userDataService;
    private final PasswordEncoder passwordEncoder;
    private final IPaymentService paymentService;
    private final UserRepository userRepository;

    public UserBusinessService(final IUserDataService userDataService,
                               final PasswordEncoder passwordEncoder,
                               final IPaymentService paymentService,
                               final UserRepository userRepository) {
        this.userDataService = userDataService;
        this.passwordEncoder = passwordEncoder;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public double addMoneyToUserBalance(AddBalanceDTO addBalanceDTO) throws UserServiceBalancePaymentException, UserServiceNotFoundException {
        try {
            log.info("Starting balance payment procedure for user with email " + addBalanceDTO.getEmail() + " for amount of " + addBalanceDTO.getAmountToAddInRealMoney() + " in currency with ISO code " + Currency.getInstance(addBalanceDTO.getLocale()).getCurrencyCode());

            User user = userDataService.getUserByEmail(addBalanceDTO.getEmail());

            PaymentProceedDTO paymentProceedDTO = new PaymentProceedDTO(
                    BigDecimal.valueOf(addBalanceDTO.getAmountToAddInRealMoney()),
                    addBalanceDTO.getPaymentNonce(),
                    addBalanceDTO.getLocale(),
                    user.getId()
            );

            String transactionId = paymentService.proceedPaymentInRealMoney(paymentProceedDTO);

            user.setBalance(user.getBalance() + addBalanceDTO.getAmountToAddInUC());

            log.info("Successfully finished balance payment procedure for user with UUID " + user.getId() + " for amount of " + addBalanceDTO.getAmountToAddInRealMoney() + " in final locale " + Currency.getInstance(addBalanceDTO.getLocale()).getCurrencyCode());
            return user.getBalance();

        } catch (UserServiceNotFoundException notFoundException) {
            log.warn("Can not add money to balance for user with email " + addBalanceDTO.getEmail() + ". " + notFoundException.getMessage());
            throw notFoundException;
        } catch (PaymentServiceException paymentServiceException) {
            log.error("Can not add money to balance of user with email " + addBalanceDTO.getEmail() + " due to payment error: " + paymentServiceException.getMessage());
            throw new UserServiceBalancePaymentException("Unable to add money to balance due to unsuccessful payment. " + paymentServiceException.getMessage(), paymentServiceException);
        }
    }

    @Override
    @Transactional
    public void changeUserPassword(ChangePasswordDTO changePasswordDTO) throws UserServicePasswordChangingException {
        try {
            log.info("Changing password for user with email " + changePasswordDTO.getEmail());

            User user = userDataService.getUserByEmail(changePasswordDTO.getEmail());

            if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Old password incorrect. ");
            }

            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

            log.info("Successfully changed password for user with email " + changePasswordDTO.getEmail());

        } catch (IllegalArgumentException illegalArgumentException) {
            log.error("User with email " + changePasswordDTO.getEmail() + " tried to change password, but old password was incorrect. ");
            throw new UserServicePasswordChangingException("Can not change password. " + illegalArgumentException.getMessage(), illegalArgumentException);
        } catch (UserServiceNotFoundException notFoundException) {
            log.error("Password change was requested for user with email " + changePasswordDTO.getEmail() + " but such user does not exist. ");
            throw new UserServicePasswordChangingException("Can not change password. " + notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    @Transactional
    public User addRoleToUser(User user, Role role) throws UserServiceValidationException {
        if (role.getRoleName().equals(ERoleName.SUPPLIER)) {
            if (userDataService.isPerson(user.getId())) {
                Person person = userDataService.getPerson(user.getId());
                if (!isPersonInfoGoodForSupplier(person)) {
                    throw new UserServiceValidationException("Person must have firstname, lastname and birthday to have role supplier. ");
                }
            }
        }

        if (role.getRoleName().equals(ERoleName.ADMIN)) {
            throw new UserServiceValidationException("User can not become admin. ");
        }

        List<Role> newRoles = user.getRoles();

        if (newRoles.stream().anyMatch(e -> e.getRoleName().equals(role.getRoleName()))) {
            throw new UserServiceValidationException("User already have role " + role.getRoleName() + ". ");
        }

        newRoles.add(role);

        log.info("giving role " + role.getRoleName() + " to user with UUID " + user.getId());

        user.setRoles(newRoles);
        userRepository.flush();

        return user;
    }

    @Override
    @Transactional
    public Person updatePersonInfo(PersonUpdateDTO dto) throws UserServiceNotFoundException, UserServiceValidationException {
        Person person = userDataService.getPerson(dto.getUserId());

        if (dto.getFirstName() == null || dto.getFirstName().isEmpty()){
            throw new UserServiceValidationException("First name can not be empty. ");
        }
        if (dto.getLastName() == null || dto.getLastName().isEmpty()){
            throw new UserServiceValidationException("Last name can not be empty. ");
        }
        if (dto.getNickName() == null || dto.getNickName().isEmpty()){
            throw new UserServiceValidationException("Nick name can not be empty. ");
        }
        if (dto.getBirthday() == null || dto.getBirthday().isAfter(LocalDate.now())){
            throw new UserServiceValidationException("Birthday can not be from future. ");
        }

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setNickName(dto.getNickName());
        person.setBirthday(dto.getBirthday());

        return person;
    }

    @Override
    @Transactional
    public Company updateCompanyInfo(CompanyUpdateDTO dto) throws UserServiceNotFoundException, UserServiceValidationException {
        Company company = userDataService.getCompany(dto.getUserId());

        if (dto.getCompanyName() == null || dto.getCompanyName().isEmpty()){
            throw new UserServiceValidationException("Company name can not be empty. ");
        }
        if (dto.getDescription() == null || dto.getDescription().isEmpty()){
            throw new UserServiceValidationException("Description can not be empty. ");
        }
        if (dto.getFoundationDate() == null || dto.getFoundationDate().isAfter(LocalDate.now())){
            throw new UserServiceValidationException("Foundation date can not be from future. ");
        }

        company.setCompanyName(dto.getCompanyName());
        company.setDescription(dto.getDescription());
        company.setFoundationDate(dto.getFoundationDate());

        return company;
    }

    private boolean isPersonInfoGoodForSupplier(Person person) {
        return person.getFirstName() != null
                && person.getLastName() != null
                && person.getBirthday() != null
                && !person.getFirstName().isEmpty()
                && !person.getLastName().isEmpty()
                && !person.getBirthday().isAfter(LocalDate.now());
    }
}
