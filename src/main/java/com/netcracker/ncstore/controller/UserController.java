package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.AddBalanceDTO;
import com.netcracker.ncstore.dto.ChangePasswordDTO;
import com.netcracker.ncstore.dto.request.UserAddBalanceRequest;
import com.netcracker.ncstore.dto.request.UserChangePasswordRequest;
import com.netcracker.ncstore.dto.response.UserAddBalanceResponse;
import com.netcracker.ncstore.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * User controller which is responsible for requests/responses
 * which are related to the user's accounts
 */
@RequestMapping(value = "/user")
@RestController
@Slf4j
public class UserController {

    private final IUserService userService;

    /**
     * Constructor
     * <p>
     * TODO: In the future, any services should be the arguments of constructor
     */
    public UserController(final IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/balance")
    @ResponseBody
    public ResponseEntity<UserAddBalanceResponse> addMoneyToOwnBalance(@RequestBody UserAddBalanceRequest request, Principal principal) {
        AddBalanceDTO addBalanceDTO = new AddBalanceDTO(
                principal.getName(),
                request.getAddAmount()
        );

        double newBalance = userService.addMoneyToBalance(addBalanceDTO);

        UserAddBalanceResponse response = new UserAddBalanceResponse(newBalance);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/password")
    @ResponseBody
    public ResponseEntity<?> changeOwnPassword(@RequestBody UserChangePasswordRequest request, Principal principal) {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(
                request.getOldPassword(),
                request.getNewPassword(),
                principal.getName()
        );

        userService.changeUserPassword(changePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
