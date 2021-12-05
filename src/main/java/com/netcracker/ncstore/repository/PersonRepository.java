package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    @Query("SELECT p FROM Person p WHERE p.user.email = ?1")
    Person findPersonByUserEmail(String email);
}