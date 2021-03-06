package com.example.interfell.family.ctrl;

import com.example.interfell.family.model.Family;
import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.Relative;
import com.example.interfell.family.service.FamilyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.springframework.data.domain.ExampleMatcher.StringMatcher.STARTING;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/family")
@Slf4j
public class FamilyCtrl {

    private static final ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(STARTING);

    private FamilyService service;

    public FamilyCtrl(FamilyService service) {
        this.service = service;
    }

    @PostMapping
    public Family createFamily(@RequestBody String name) {
        return service.createFamily(name.trim());
    }

    @GetMapping
    public List<Family> getFamilies(@RequestParam(required = false) String filter) {

        if (Objects.isNull(filter))
            return service.findFamilies();
        return service.findFamilies(Example.of(new Family(filter), matcher));
    }

    @GetMapping("/{id}")
    public Family getFamily(@PathVariable Long id) {
        return service.findFamily(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("family with id '%d' not found", id)));
    }

    @PostMapping("/{family}/rel/{relation}")
    public Family addRelative(@PathVariable Long family, @PathVariable Long relation) {
        Family f = service.findFamily(family)
                .orElseThrow(() -> new EntityNotFoundException(String.format("family with id '%d' not found", family)));

        Relative r = service.findRelative(relation)
                .orElseThrow(() -> new EntityNotFoundException(String.format("relation with id '%d' not found", family)));

        return service.addRelatives(f, r);
    }

    @GetMapping("/{id}/members")
    public Set<Person> getFamilyMembers(@PathVariable Long id) {
        return service.findFamily(id)
                .map(f -> f.getRelatives().stream()
                        .map(r -> List.of(r.getOrigin(), r.getDestination()))
                        .flatMap(List::stream)
                        .peek(p -> log.info("{}", p))
                        .collect(toSet())
                )
                .orElseThrow(() -> new EntityNotFoundException(String.format("family with id '%d' not found", id)));
    }
}
