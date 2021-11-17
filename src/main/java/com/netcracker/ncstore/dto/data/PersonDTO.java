package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Contains all information about real Person entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */
@Getter
@AllArgsConstructor
public class PersonDTO {
    private final UUID userId;
    private final String firstName;
    private final String lastName;
    private final String nickName;
    private final LocalDate birthday;

    public PersonDTO(Person person) {
        userId = person.getUserId();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        nickName = person.getNickName();
        birthday = person.getBirthday();
    }
}
