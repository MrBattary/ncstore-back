package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Class that defines a role of user in the system. User can have multiple roles.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {//need to implement GrantedAuthority, but no spring security yet
    private long id;
    private String name;

    private List<User> users;
}
