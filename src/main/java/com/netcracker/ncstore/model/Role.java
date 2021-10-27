package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Class that defines a role of user in the system. User can have multiple roles.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {//need to implement GrantedAuthority, but no spring security yet
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
