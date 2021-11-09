package com.netcracker.ncstore.service.user;

import com.netcracker.ncstore.dto.UserModelWithoutIdDTO;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.UserRepository;
import com.netcracker.ncstore.service.role.IRoleService;
import com.netcracker.ncstore.util.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private final IRoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger log;

    public UserService(final IRoleService roleService,
                       final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        log = LoggerFactory.getLogger(UserService.class);
    }

    @Override
    public void checkUserEmail(final String email) throws UserServiceValidationException, UserServiceRepositoryException {
        if (!EmailValidator.isEmailValid(email)) {
            throw new UserServiceValidationException("The email: " + email + " isn't valid");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserServiceRepositoryException("User with email: " + email + " already exists");
        }
    }

    @Override
    public User buildUserFromUserModelDTO(UserModelWithoutIdDTO userModel) throws UserServiceBuildingException {
        try {
            log.info("The building of user with an email: " + userModel.getEmail() + " begins");
            checkUserEmail(userModel.getEmail());
            User user = new User(
                    userModel.getEmail(),
                    passwordEncoder.encode(userModel.getPassword()),
                    userModel.getBalance(),
                    roleService.buildRolesList(userModel.getRoleNames())
            );
            log.info("The building of user with an email: " + userModel.getEmail() + " completed successfully");
            return user;
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceBuildingException("Unable to build a user with email: " + userModel.getEmail(), e);
        }
    }

}
