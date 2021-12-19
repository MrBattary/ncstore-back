package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.exception.UserServiceBalancePaymentException;
import com.netcracker.ncstore.exception.UserServiceChangePasswordException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServicePasswordChangingException;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.service.payment.interfaces.IPaymentService;
import com.netcracker.ncstore.service.user.interfaces.IUserBusinessService;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Currency;

@Service
@Slf4j
public class UserBusinessService implements IUserBusinessService {
    private final IUserDataService userDataService;
    private final PasswordEncoder passwordEncoder;
    private final IPaymentService paymentService;

    public UserBusinessService(final IUserDataService userDataService,
                               final PasswordEncoder passwordEncoder,
                               final IPaymentService paymentService) {
        this.userDataService = userDataService;
        this.passwordEncoder = passwordEncoder;
        this.paymentService = paymentService;
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

            log.info("Successfully finished balance payment procedure for user with UUID " + user.getId() + " for amount of " + addBalanceDTO.getAmountToAddInRealMoney() + " in final locale "+Currency.getInstance(addBalanceDTO.getLocale()).getCurrencyCode());
            return user.getBalance();

        } catch (UserServiceNotFoundException notFoundException) {
            log.warn("Can not add money to balance for user with email " + addBalanceDTO.getEmail() + ". " + notFoundException.getMessage());
            throw notFoundException;
        } catch (PaymentServiceException paymentServiceException) {
            log.error("Can not add money to balance of user with email " + addBalanceDTO.getEmail() + " due to payment error: " + paymentServiceException.getMessage());
            throw new UserServiceChangePasswordException("Unable to add money to balance due to unsuccessful payment. " + paymentServiceException.getMessage(), paymentServiceException);
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
            log.error("User with email " + changePasswordDTO.getEmail() + " tried to change password, but old password was incorrect");
            throw new UserServiceChangePasswordException("Can not change password. " + illegalArgumentException.getMessage(), illegalArgumentException);
        } catch (UserServiceNotFoundException notFoundException) {
            log.error("Password change was requested for user with email " + changePasswordDTO.getEmail() + " but such user does not exist. ");
            throw new UserServiceChangePasswordException("Can not change password. " + notFoundException.getMessage(), notFoundException);
        }
    }
}
