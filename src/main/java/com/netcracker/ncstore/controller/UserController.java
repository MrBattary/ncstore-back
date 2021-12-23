package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.UserAddBalanceBody;
import com.netcracker.ncstore.dto.body.UserChangePasswordBody;
import com.netcracker.ncstore.dto.body.UserGainRoleBody;
import com.netcracker.ncstore.dto.request.UserAddBalanceRequest;
import com.netcracker.ncstore.dto.request.UserAddRoleRequest;
import com.netcracker.ncstore.dto.request.UserBalanceGetRequest;
import com.netcracker.ncstore.dto.request.UserChangePasswordRequest;
import com.netcracker.ncstore.dto.response.UserAddBalanceResponse;
import com.netcracker.ncstore.dto.response.UserBalanceGetResponse;
import com.netcracker.ncstore.service.web.user.IUserBaseWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private final IUserBaseWebService userWebService;

    public UserController(IUserBaseWebService userWebService) {
        this.userWebService = userWebService;
    }


    @PostMapping(value = "/balance")
    public ResponseEntity<UserAddBalanceResponse> addMoneyToOwnBalance(@RequestBody final UserAddBalanceBody body,
                                                                       final Principal principal,
                                                                       final Locale locale) {
        log.info("REQUEST: to add " + body.getPaymentAmount() + " money in locale " + locale.toLanguageTag() + " to balance for user with email " + principal.getName());

        UserAddBalanceRequest request = new UserAddBalanceRequest(
                body.getPaymentAmount(),
                body.getNonce(),
                principal.getName(),
                locale
        );

        UserAddBalanceResponse response = userWebService.addMoneyToUserBalance(request);

        log.info("RESPONSE: to add " + request.getPaymentAmount() + " money in locale " + locale.toLanguageTag() + " to balance for user with email " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @GetMapping(value = "/balance")
    public ResponseEntity<UserBalanceGetResponse> getUserBalance(final Principal principal,
                                                                 final Locale locale) {

        log.info("REQUEST: to get balance for user with email " + principal.getName());

        UserBalanceGetRequest request = new UserBalanceGetRequest(
                principal.getName(),
                principal.getName(),
                locale
        );

        UserBalanceGetResponse response = userWebService.getBalanceOfUser(request);

        log.info("RESPONSE: to get balance for user with email " + principal.getName());

        return ResponseEntity.
                ok().
                body(response);
    }

    @PostMapping(value = "/password")
    public ResponseEntity<?> changeOwnPassword(@RequestBody final UserChangePasswordBody body,
                                               final Principal principal) {

        log.info("REQUEST: to change password from user with email " + principal.getName());

        UserChangePasswordRequest request = new UserChangePasswordRequest(
                body.getOldPassword(),
                body.getNewPassword(),
                principal.getName()
        );

        userWebService.changePasswordForUser(request);

        log.info("RESPONSE: to change password from user with email " + principal.getName());

        return ResponseEntity.
                noContent().
                build();
    }

    @PostMapping(value = "/gainrole")
    public ResponseEntity<?> gainNewRole(@RequestBody UserGainRoleBody body,
                                         final Principal principal) {
        log.info("REQUEST: to gain new role " + body.getRoleName() + " from user with email " + principal.getName());

        UserAddRoleRequest request = new UserAddRoleRequest(
                principal.getName(),
                body.getRoleName().toUpperCase(Locale.ROOT)
        );

        userWebService.addRoleToUser(request);

        log.info("RESPONSE: to gain new role " + body.getRoleName() + " from user with email " + principal.getName());
        return ResponseEntity.
                noContent().
                build();
    }

}
