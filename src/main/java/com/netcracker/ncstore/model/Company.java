package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class that defines company data of user of the system.
 * Used if user is a company, not an individual.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    private long userId;
    private String companyName;
    private String description;
    private LocalDate foundationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

}
