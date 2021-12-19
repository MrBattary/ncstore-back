package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.LoginSuccessDTO;
import com.netcracker.ncstore.dto.UserEmailAndRolesDTO;
import com.netcracker.ncstore.dto.UserEmailPasswordSignInDTO;
import com.netcracker.ncstore.exception.UserServiceLoginException;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.security.IJwtTokenService;
import com.netcracker.ncstore.service.user.interfaces.IUserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginBusinessService implements IUserLoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtTokenService jwtTokenService;

    public LoginBusinessService(final UserRepository userRepository,
                                final PasswordEncoder passwordEncoder,
                                final IJwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public LoginSuccessDTO loginUsingEmailAndPassword(UserEmailPasswordSignInDTO request) throws UserServiceLoginException {
        log.info("Started login operation for user with email "+request.getEmail());
        try {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(IllegalArgumentException::new);

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException();
            }

            String token = getToken(user);

            log.info("Login operation for user with email "+request.getEmail() +" completed successfully");

            return new LoginSuccessDTO(
                    token,
                    user
            );

        } catch (IllegalArgumentException e) {
            log.error("User with email " + request.getEmail() + " entered incorrect login details");
            throw new UserServiceLoginException("Incorrect login details. ");
        }
    }

    private String getToken(User user) {
        log.info("Generating token for user with email "+user.getEmail());
        return jwtTokenService.createToken(
                new UserEmailAndRolesDTO(
                        user.getEmail(),
                        user.getRoles().
                                stream().
                                map(Role::getRoleName).
                                collect(Collectors.toList())
                )
        );
    }
}
