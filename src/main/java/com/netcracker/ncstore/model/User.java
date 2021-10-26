package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Class that defines a user of the system
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User { //need to implement UserDetails but no spring security yet
    private long id;
    private String login;
    private String password;
    private double balance;

    private List<Role> roles;
}
