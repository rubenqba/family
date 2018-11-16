package com.example.interfell.family.ctrl;

import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.PersonRelatives;
import com.example.interfell.family.model.RelType;
import com.example.interfell.family.model.Relative;
import com.example.interfell.family.service.FamilyService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;
import static org.springframework.data.domain.ExampleMatcher.StringMatcher.STARTING;

@RestController
@RequestMapping("/people")
public class PeopleCtrl {

    private static final ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(STARTING);

    private static final ExampleMatcher relMatcher = ExampleMatcher.matchingAny()
            .withIgnoreNullValues();

    private static final Pattern timestamp = Pattern.compile("-?[1-9]([0-9]+)?");

    private FamilyService service;


    public PeopleCtrl(FamilyService service) {
        this.service = service;
    }

    @PostMapping
    public Person createPerson(@RequestBody Person p) {
        return service.savePerson(p);
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person p) {
        return service.findPerson(id)
                .map(found -> {
                    p.setId(found.getId());
                    return service.savePerson(p);
                })
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", id)));
    }

    @GetMapping
    public List<Person> getPeople(@RequestParam(required = false) String filter) {
        if (Objects.isNull(filter))
            return service.findPeople();

        Person like = getQuery(filter);
        return service.findPeople(Example.of(like, matcher));
    }

    @GetMapping("/{id}")
    public Person getPeople(@PathVariable Long id) {
        return service.findPerson(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", id)));
    }

    private Person getQuery(String filter) {
        Person like = new Person();
        like.setFirstName(filter.trim());
        like.setLastName(filter.trim());
        if(isNumeric(filter.trim()))
            like.setBirthDay(Instant.ofEpochSecond(Long.valueOf(filter.trim())));
        return like;
    }

    private boolean isNumeric(String value) {
        return timestamp.matcher(value).matches();
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        service.deletePerson(id);
    }

    @PostMapping("/{from}/rel/{relation}/{to}")
    public List<Relative> addRelative(@PathVariable Long from, @PathVariable Long to, @PathVariable RelType relation) {
        Person origin = service.findPerson(from)
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", from)));

        Person target = service.findPerson(to)
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", to)));

        Relative rel = new Relative();
        rel.setOrigin(origin);
        rel.setType(relation);
        rel.setDestination(target);
        Relative first = service.saveRelative(rel);
        if (relation.isBidirectional()) {
            rel = new Relative();
            rel.setOrigin(target);
            rel.setType(relation);
            rel.setDestination(origin);
            return List.of(first, service.saveRelative(rel));
        }
        return List.of(first);
    }

    @GetMapping("/{person}/rel")
    public PersonRelatives relativesOf(@PathVariable Long person) {
        Person subject = service.findPerson(person)
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", person)));

        List<Relative> relatives = service.findRelatives(subject);

        Map<String, Map<RelType, Set<Person>>> rels = new HashMap<>();

        return PersonRelatives.builder()
                .relatedWith(relatives.stream()
                        .filter(r -> r.getType().isBidirectional())
                        .map(r -> {
                            if (subject.equals(r.getOrigin()))
                                return new SimpleEntry<>(r.getType(), r.getDestination());
                            else
                                return new SimpleEntry<>(r.getType(), r.getOrigin());
                        })
                        .collect(groupingBy(SimpleEntry::getKey, mapping(SimpleEntry::getValue, toSet()))))
                .relativeOf(relatives.stream()
                        .filter(r -> !r.getType().isBidirectional() && subject.equals(r.getOrigin()))
                        .collect(groupingBy(Relative::getType, mapping(Relative::getDestination, toSet()))))
                .inverseRelativeOf(relatives.stream()
                        .filter(r -> !r.getType().isBidirectional() && subject.equals(r.getDestination()))
                        .collect(groupingBy(Relative::getType, mapping(Relative::getOrigin, toSet()))))
                .build();
    }
}
