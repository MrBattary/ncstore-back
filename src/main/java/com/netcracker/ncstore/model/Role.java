package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Class that defines a role of user in the system. User can have multiple roles.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {//need to implement GrantedAuthority, but no spring security yet
    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERoleName roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
