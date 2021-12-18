package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.request.UserAddBalanceRequest;
import com.netcracker.ncstore.dto.request.UserChangePasswordRequest;
import com.netcracker.ncstore.dto.response.UserAddBalanceResponse;
import com.netcracker.ncstore.dto.response.UserBalanceResponse;
import com.netcracker.ncstore.service.priceconverter.interfaces.IPriceConversionService;
import com.netcracker.ncstore.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Locale;

/**
 * User controller which is responsible for requests/responses
 * which are related to the user's accounts
 */
@RequestMapping(value = "/user")
@RestController
@Slf4j
public class UserController {

    private final IUserService userService;
    private final IPriceConversionService priceConversionService;

    public UserController(final IUserService userService,
                          final IPriceConversionService priceConversionService) {
        this.userService = userService;
        this.priceConversionService = priceConversionService;
    }

    @PostMapping(value = "/balance")
    public ResponseEntity<UserAddBalanceResponse> addMoneyToOwnBalance(@RequestBody final UserAddBalanceRequest request,
                                                                       final Principal principal,
                                                                       final Locale locale) {
        log.info("REQUEST: to add " + request.getPaymentAmount() + " money in locale " + locale.toLanguageTag() + " to balance for user with email " + principal.getName());

        AddBalanceDTO addBalanceDTO = new AddBalanceDTO(
                principal.getName(),
                request.getPaymentAmount(),
                request.getNonce(),
                locale
        );

        double newBalance = userService.addMoneyToUserBalance(addBalanceDTO);

        ConvertedPriceWithCurrencySymbolDTO convertedBalance = priceConversionService.convertUCPriceToRealPriceWithSymbol(
                newBalance,
                locale
        );

        UserAddBalanceResponse response = new UserAddBalanceResponse(
                convertedBalance.getPrice(),
                convertedBalance.getSymbol()
        );

        log.info("RESPONSE: to add " + request.getPaymentAmount() + " money in locale " + locale.toLanguageTag() + " to balance for user with email " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<UserBalanceResponse> getUserBalance(final Principal principal,
                                                              final Locale locale) {

        log.info("REQUEST: to get balance for user with email " + principal.getName());

        double balance = userService.getUserBalance(principal.getName());

        ConvertedPriceWithCurrencySymbolDTO convertedBalance =
                priceConversionService.convertUCPriceToRealPriceWithSymbol(balance, locale);

        UserBalanceResponse response = new UserBalanceResponse(
                convertedBalance.getPrice(),
                convertedBalance.getSymbol()
        );

        log.info("RESPONSE: to get balance for user with email " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @PostMapping(value = "/password")
    public ResponseEntity<?> changeOwnPassword(@RequestBody final UserChangePasswordRequest request,
                                               final Principal principal) {

        log.info("REQUEST: to change password from user with email " + principal.getName());

        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                request.getOldPassword(),
                request.getNewPassword(),
                principal.getName()
        );

        userService.changeUserPassword(changePasswordDTO);

        log.info("RESPONSE: to change password from user with email " + principal.getName());

        return ResponseEntity.
                noContent().
                build();
    }

}
