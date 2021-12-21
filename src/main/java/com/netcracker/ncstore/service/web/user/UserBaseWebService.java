package com.netcracker.ncstore.service.web.user;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.UCPriceConvertedFromRealDTO;
import com.netcracker.ncstore.dto.request.UserAddBalanceRequest;
import com.netcracker.ncstore.dto.request.UserAddRoleRequest;
import com.netcracker.ncstore.dto.request.UserBalanceGetRequest;
import com.netcracker.ncstore.dto.request.UserChangePasswordRequest;
import com.netcracker.ncstore.dto.response.UserAddBalanceResponse;
import com.netcracker.ncstore.dto.response.UserBalanceGetResponse;
import com.netcracker.ncstore.exception.RoleServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServiceBalancePaymentException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServicePasswordChangingException;
import com.netcracker.ncstore.exception.UserServiceValidationException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.service.general.priceconverter.IPriceConversionService;
import com.netcracker.ncstore.service.data.role.IRoleDataService;
import com.netcracker.ncstore.service.business.user.IUserBusinessService;
import com.netcracker.ncstore.service.data.user.IUserDataService;
import com.netcracker.ncstore.util.converter.DoubleRounder;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserBaseWebService implements IUserBaseWebService {
    private final IUserBusinessService userBusinessService;
    private final IUserDataService userDataService;
    private final IPriceConversionService priceConversionService;
    private final IRoleDataService roleDataService;

    public UserBaseWebService(final IUserBusinessService userBusinessService,
                              final IUserDataService userDataService,
                              final IPriceConversionService priceConversionService,
                              final IRoleDataService roleDataService) {
        this.userBusinessService = userBusinessService;
        this.userDataService = userDataService;
        this.priceConversionService = priceConversionService;
        this.roleDataService = roleDataService;
    }

    @Override
    public UserAddBalanceResponse addMoneyToUserBalance(UserAddBalanceRequest request) throws GeneralBadRequestException, GeneralNotFoundException {
        try {
            UCPriceConvertedFromRealDTO UCAmountToAdd = priceConversionService.convertRealPriceToUC(
                    request.getPaymentAmount(),
                    request.getLocale()
            );

            AddBalanceDTO addBalanceDTO = new AddBalanceDTO(
                    request.getEmail(),
                    request.getPaymentAmount(),
                    UCAmountToAdd.getUCAmount(),
                    request.getNonce(),
                    request.getLocale()
            );

            double newBalance = userBusinessService.addMoneyToUserBalance(addBalanceDTO);

            ConvertedPriceWithCurrencySymbolDTO convertedBalance = priceConversionService.convertUCPriceToRealPriceWithSymbol(
                    newBalance,
                    request.getLocale()
            );

            return new UserAddBalanceResponse(
                    DoubleRounder.round(convertedBalance.getPrice(), 2),
                    LocaleToCurrencyConverter.getCurrencySymbolByLocale(UCAmountToAdd.getActualLocale())
            );
        } catch (UserServiceBalancePaymentException paymentException) {
            throw new GeneralBadRequestException(paymentException.getMessage(), paymentException);
        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public void changePasswordForUser(UserChangePasswordRequest request) throws GeneralBadRequestException {
        try {
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                    request.getOldPassword(),
                    request.getNewPassword(),
                    request.getEmail()
            );

            userBusinessService.changeUserPassword(changePasswordDTO);
        } catch (UserServicePasswordChangingException e) {
            throw new GeneralBadRequestException(e.getMessage(), e);
        }
    }

    @Override
    public UserBalanceGetResponse getBalanceOfUser(UserBalanceGetRequest request) throws GeneralNotFoundException, GeneralPermissionDeniedException {
        try {
            User user = userDataService.getUserByEmail(request.getEmailOfUser());

            if (!user.getEmail().equals(request.getEmailOfIssuer())) {
                throw new GeneralPermissionDeniedException("Only user can view self balance. ");
            }

            ConvertedPriceWithCurrencySymbolDTO convertedBalance = priceConversionService.convertUCPriceToRealPriceWithSymbol(
                    user.getBalance(),
                    request.getLocale()
            );

            return new UserBalanceGetResponse(
                    DoubleRounder.round(convertedBalance.getPrice(), 2),
                    convertedBalance.getSymbol()
            );

        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public void addRoleToUser(UserAddRoleRequest request) throws GeneralNotFoundException, GeneralBadRequestException {
        try {
            User user = userDataService.getUserByEmail(request.getEmail());
            Role roleToAdd = roleDataService.getRoleByName(request.getNameOfNewuserRole());

            userBusinessService.addRoleToUser(user, roleToAdd);

        } catch (UserServiceNotFoundException | RoleServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        } catch (UserServiceValidationException validationException) {
            throw new GeneralBadRequestException(validationException.getMessage(), validationException);
        }
    }
}
