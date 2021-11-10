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
    public User buildUserFromUserModelDTO(UserModelWithoutIdDTO userModelDTO) throws UserServiceBuildingException {
        try {
            log.info("The building of user with an email: " + userModelDTO.getEmail() + " begins");
            checkUserEmail(userModelDTO.getEmail());
            User user = new User(
                    userModelDTO.getEmail(),
                    passwordEncoder.encode(userModelDTO.getPassword()),
                    userModelDTO.getBalance(),
                    roleService.buildRolesList(userModelDTO.getRoleNames())
            );
            log.info("The building of user with an email: " + userModelDTO.getEmail() + " completed successfully");
            return user;
        } catch (UserServiceValidationException | UserServiceRepositoryException e) {
            log.error(e.getMessage());
            throw new UserServiceBuildingException("Unable to build a user with email: " + userModelDTO.getEmail(), e);
        }
    }

}
