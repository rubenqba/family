package com.example.interfell.family.service.impl;

import com.example.interfell.family.model.Family;
import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.Relative;
import com.example.interfell.family.service.FamilyRepository;
import com.example.interfell.family.service.FamilyService;
import com.example.interfell.family.service.PeopleRepository;
import com.example.interfell.family.service.RelativeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FamilyServiceImpl implements FamilyService {

    private FamilyRepository families;
    private PeopleRepository people;
    private RelativeRepository relatives;

    public FamilyServiceImpl(FamilyRepository families, PeopleRepository people, RelativeRepository relatives) {
        this.families = families;
        this.people = people;
        this.relatives = relatives;
    }

    @Override
    public Family createFamily(String name) {
        return families.save(new Family(name));
    }

    @Override
    public void deleteFamily(Long id) {
        families.deleteById(id);
    }

    @Override
    public Optional<Family> findFamily(Long id) {
        return families.findById(id);
    }

    @Override
    public Optional<Family> findFamily(Example<Family> example) {
        return families.findOne(example);
    }

    @Override
    public List<Family> findFamilies() {
        return families.findAll(Sort.by("name", "id"));
    }

    @Override
    public List<Family> findFamilies(Example<Family> example) {
        return families.findAll(example, Sort.by("name", "id"));
    }

    @Override
    public Person savePerson(Person p) {
        return people.save(p);
    }

    @Override
    public void deletePerson(Long id) {
        people.deleteById(id);
    }

    @Override
    public Optional<Person> findPerson(Long id) {
        return people.findById(id);
    }

    @Override
    public Optional<Person> findPerson(Example<Person> example) {
        return people.findOne(example);
    }

    @Override
    public List<Person> findPeople() {
        return people.findAll(Sort.by("firstName", "lastName", "birthDay", "sex", "id"));
    }

    @Override
    public List<Person> findPeople(Example<Person> example) {
        return people.findAll(example, Sort.by("firstName", "lastName", "birthDay", "sex", "id"));
    }

    @Override
    public Relative saveRelative(Relative relative) {
        return relatives.save(relative);
    }

    @Override
    public Optional<Relative> findRelative(Long id) {
        return relatives.findById(id);
    }

    @Override
    public Optional<Relative> findRelative(Example<Relative> example) {
        return relatives.findOne(example);
    }

    @Override
    public List<Relative> findRelatives(Person of) {
        return relatives.findAllByOriginIsOrDestinationIs(of, of, Sort.by("type", "origin", "destination"));
    }

    @Override
    public Family addRelatives(Family family, Relative relative) {
        family.addRelative(relative);
        return families.save(family);
    }
}
