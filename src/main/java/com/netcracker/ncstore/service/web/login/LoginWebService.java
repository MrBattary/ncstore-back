package com.netcracker.ncstore.service.web.login;

import com.netcracker.ncstore.dto.LoginSuccessDTO;
import com.netcracker.ncstore.dto.UserEmailPasswordSignInDTO;
import com.netcracker.ncstore.dto.request.SignInEmailPasswordRequest;
import com.netcracker.ncstore.dto.response.SignInResponse;
import com.netcracker.ncstore.exception.UserServiceLoginException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.model.enumerations.EUserType;
import com.netcracker.ncstore.service.data.user.IUserDataService;
import com.netcracker.ncstore.service.business.login.ILoginBusinessService;
import com.netcracker.ncstore.util.converter.RolesConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginWebService implements ILoginWebService {
    private final ILoginBusinessService userLoginService;
    private final IUserDataService userDataService;

    public LoginWebService(final ILoginBusinessService userLoginService,
                           final IUserDataService userDataService) {
        this.userLoginService = userLoginService;
        this.userDataService = userDataService;
    }

    @Override
    public SignInResponse signInByEmailAndPassword(SignInEmailPasswordRequest request) {
        try {

            UserEmailPasswordSignInDTO signInDTO = new UserEmailPasswordSignInDTO(
                    request.getEmail(),
                    request.getNotCodedPassword()
            );

            LoginSuccessDTO loginSuccessDTO = userLoginService.loginUsingEmailAndPassword(signInDTO);

            EUserType userType = userDataService.isCompany(loginSuccessDTO.getUser().getId()) ? EUserType.COMPANY : EUserType.PERSON;

            return new SignInResponse(
                    loginSuccessDTO.getUser().getId(),
                    userType,
                    RolesConverter.rolesListToRoleNamesList(loginSuccessDTO.getUser().getRoles()),
                    loginSuccessDTO.getToken()
            );
        } catch (UserServiceLoginException loginException) {
            throw new GeneralBadRequestException(loginException.getMessage(), loginException);
        }
    }
}
