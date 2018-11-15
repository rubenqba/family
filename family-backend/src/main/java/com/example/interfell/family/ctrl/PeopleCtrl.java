package com.example.interfell.family.ctrl;

import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.RelType;
import com.example.interfell.family.model.Relative;
import com.example.interfell.family.service.FamilyService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static org.springframework.data.domain.ExampleMatcher.StringMatcher.STARTING;

@RestController
@RequestMapping("/people")
public class PeopleCtrl {

    private static final ExampleMatcher matcher = ExampleMatcher.matchingAny()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(STARTING);

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
        Person like = new Person();
        like.setId(id);
        return service.findPerson(Example.of(like))
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
        Person like = new Person();
        like.setId(id);
        return service.findPerson(Example.of(like))
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
    public Relative addRelative(@PathVariable Long from, @PathVariable Long to, @PathVariable RelType relation) {
        Person like = new Person();
        like.setId(from);

        Person origin = service.findPerson(Example.of(like))
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", from)));

        like.setId(to);
        Person target = service.findPerson(Example.of(like))
                .orElseThrow(() -> new EntityNotFoundException(String.format("person with id '%d' not found", to)));

        Relative rel = new Relative();
        rel.setOrigin(origin);
        rel.setType(relation);
        rel.setDestination(target);
        return service.saveRelative(rel);
    }
}
