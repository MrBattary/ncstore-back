package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.UUID;


/**
 * Class that defines a user of the system
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User { //need to implement UserDetails but no spring security yet
    @Id
    @GeneratedValue
    private UUID id;
    private String email;
    private String password;
    private double balance;

    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

}
