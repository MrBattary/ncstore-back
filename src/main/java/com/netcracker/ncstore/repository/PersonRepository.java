package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
}