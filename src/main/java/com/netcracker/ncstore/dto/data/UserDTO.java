package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.User;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class UserDTO {
    private final UUID id;
    private final String email;
    private final String password;
    private final double balance;
    private final List<UUID> rolesIds;

    public UserDTO(User user){
        id = user.getId();
        email = user.getEmail();
        password = user.getPassword();
        balance = user.getBalance();
        rolesIds = user.getRoles().
                stream().
                map(Role::getId).
                collect(Collectors.toList());
    }
}
