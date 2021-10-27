package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class that defines personal data of user of the system.
 * Used for user if one is an individual (real-life human).
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    private long userId;
    private String firstName;
    private String secondName;
    private String nickName;
    private LocalDate birthday;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;
}
