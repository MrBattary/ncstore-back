package com.netcracker.ncstore.service.user.web;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.UCPriceConvertedFromRealDTO;
import com.netcracker.ncstore.dto.request.UserAddBalanceRequest;
import com.netcracker.ncstore.dto.request.UserBalanceGetRequest;
import com.netcracker.ncstore.dto.request.UserChangePasswordRequest;
import com.netcracker.ncstore.dto.response.UserAddBalanceResponse;
import com.netcracker.ncstore.dto.response.UserBalanceGetResponse;
import com.netcracker.ncstore.exception.UserServiceBalancePaymentException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
import com.netcracker.ncstore.exception.UserServicePasswordChangingException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.service.priceconverter.interfaces.IPriceConversionService;
import com.netcracker.ncstore.service.user.interfaces.IUserBusinessService;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import com.netcracker.ncstore.service.user.interfaces.web.IUserBaseWebService;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserBaseWebService implements IUserBaseWebService {
    private final IUserBusinessService userBusinessService;
    private final IUserDataService userDataService;
    private final IPriceConversionService priceConversionService;

    public UserBaseWebService(final IUserBusinessService userBusinessService,
                              final IUserDataService userDataService,
                              final IPriceConversionService priceConversionService) {
        this.userBusinessService = userBusinessService;
        this.userDataService = userDataService;
        this.priceConversionService = priceConversionService;
    }

    @Override
    public UserAddBalanceResponse addMoneyToUserBalance(UserAddBalanceRequest request) {
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

            return new UserAddBalanceResponse(
                    newBalance,
                    LocaleToCurrencyConverter.getCurrencySymbolByLocale(UCAmountToAdd.getActualLocale())
            );
        } catch (UserServiceBalancePaymentException e) {
            throw new GeneralBadRequestException(e.getMessage(), e);
        }
    }

    @Override
    public void changePasswordForUser(UserChangePasswordRequest request) {
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
    public UserBalanceGetResponse getBalanceOfUser(UserBalanceGetRequest request) {
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
                    user.getBalance(),
                    convertedBalance.getSymbol()
            );

        } catch (UserServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }
}
