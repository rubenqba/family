package com.example.interfell.family.service;

import com.example.interfell.family.model.Family;
import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.Relative;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface FamilyService {

    Family createFamily(String name);

    void deleteFamily(Long id);

    Optional<Family> findFamily(Long id);

    Optional<Family> findFamily(Example<Family> example);

    List<Family> findFamilies();

    List<Family> findFamilies(Example<Family> example);

    Person savePerson(Person p);

    void deletePerson(Long id);

    Optional<Person> findPerson(Long id);

    Optional<Person> findPerson(Example<Person> example);

    List<Person> findPeople();

    List<Person> findPeople(Example<Person> example);

    Relative saveRelative(Relative relative);

    Optional<Relative> findRelative(Long id);

    Optional<Relative> findRelative(Example<Relative> of);

    List<Relative> findRelatives(Person of);

    Family addRelatives(Family family, Relative relative);
}
