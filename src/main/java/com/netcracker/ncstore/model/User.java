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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    //TODO: need to implement UserDetails but no spring security yet
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

    /**
     * Constructor
     * @param email - email
     * @param password - encoded password
     * @param balance - balance
     * @param roles - list of roles
     */
    public User(final String email,
                final String password,
                final double balance,
                final List<Role> roles) {
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User){
            return ((User)obj).getId().equals(id);
        }else{
            return false;
        }
    }
}
