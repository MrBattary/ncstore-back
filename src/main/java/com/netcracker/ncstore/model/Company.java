package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Class that defines company data of user of the system.
 * Used if user is a company, not an individual.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    private UUID userId;
    private String companyName;
    @Lob
    private String description;
    private LocalDate foundationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    public Company(String companyName, String description, LocalDate foundationDate, User user) {
        this.companyName = companyName;
        this.description = description;
        this.foundationDate = foundationDate;
        this.user = user;
    }
}
