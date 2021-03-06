package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Class that defines personal data of user of the system.
 * Used for user if one is an individual (real-life human).
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    private UUID userId;
    private String firstName;
    private String lastName;
    private String nickName;
    private LocalDate birthday;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    public Person(String firstName, String lastName, String nickName, LocalDate birthday, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.birthday = birthday;
        this.user = user;
    }
}
