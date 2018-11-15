package com.example.interfell.family.service;

import com.example.interfell.family.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<Person, Long> {
}
