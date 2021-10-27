package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}