package com.example.interfell.family.service;

import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.Relative;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelativeRepository extends JpaRepository<Relative, Long> {

    List<Relative> findAllByOriginIsOrDestinationIs(Person orig, Person dest, Sort sort);
}
