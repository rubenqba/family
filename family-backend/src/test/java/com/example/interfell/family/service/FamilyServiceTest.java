package com.example.interfell.family.service;

import com.example.interfell.family.model.Family;
import com.example.interfell.family.model.Person;
import com.example.interfell.family.model.RelType;
import com.example.interfell.family.model.Relative;
import com.example.interfell.family.service.impl.FamilyServiceImpl;
import net.bytebuddy.implementation.bytecode.assign.InstanceCheck;
import net.bytebuddy.utility.RandomString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.interfell.family.model.RelType.PARENT;
import static com.example.interfell.family.model.Sex.FEMALE;
import static com.example.interfell.family.model.Sex.MALE;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FamilyServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FamilyRepository families;

    @Autowired
    private PeopleRepository people;

    @Autowired
    private RelativeRepository relatives;

    private FamilyService service;


    @Before
    public void setUp() throws Exception {
        service = new FamilyServiceImpl(families, people, relatives);
    }

    @Test
    public void createFamily() {
        String fname = "bresler-pelaez";
        Family f = service.createFamily(fname);

        assertThat(f, allOf(
                notNullValue(),
                allOf(
                        hasProperty("id", greaterThan(0L)),
                        hasProperty("name", is(fname)),
                        hasProperty("relatives", empty())
                )
        ));
    }

    @Test
    public void findFamily() {

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnorePaths("relatives")
                .withIgnoreNullValues()
                .withMatcher("id", exact())
                .withIgnoreCase()
                .withMatcher("name", exact());

        String fname = "bresler-pelaez";
        assertThat(service.createFamily(fname), notNullValue());

        Family test = new Family(fname);
        Optional<Family> f = service.findFamily(Example.of(test, matcher));

        assertThat(f.isPresent(), is(true));
        assertThat(f.get(), allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is(fname)),
                hasProperty("relatives", empty())
        ));

        test = new Family();
        test.setId(1L);
        f = service.findFamily(Example.of(test, matcher));

        assertThat(f.isPresent(), is(true));
        assertThat(f.get(), allOf(
                hasProperty("id", is(1L)),
                hasProperty("name", is(fname)),
                hasProperty("relatives", empty())
        ));

        matcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("relatives")
                .withIgnoreNullValues()
                .withMatcher("id", exact())
                .withIgnoreCase()
                .withMatcher("name", exact());

        test = new Family();
        test.setId(2L);
        test.setName(fname);
        f = service.findFamily(Example.of(test, matcher));

        assertThat(f.isPresent(), is(false));
    }

    @Test
    public void findFamilies() {
        for (int i = 0; i < 5; i++) {
            service.createFamily((i%2==0 ? "F" : "R") + i);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("id", "relatives")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith());

        List<Family> list = service.findFamilies(Example.of(new Family("f"), matcher));
        assertThat(list, hasSize(3));
    }

    @Test
    public void savePerson() {
        Person p = new Person();
        p.setFirstName("Maria");
        p.setLastName("González López");
        p.setBirthDay(Instant.now());
        p.setSex(FEMALE);

        Person t = service.savePerson(p);

        assertThat(t, allOf(
                notNullValue(),
                allOf(
                        hasProperty("id", greaterThan(0L)),
                        hasProperty("firstName", is(p.getFirstName())),
                        hasProperty("lastName", is(p.getLastName())),
                        hasProperty("birthDay", is(p.getBirthDay())),
                        hasProperty("sex", is(p.getSex()))
                )
        ));
    }

    @Test
    public void findPerson() {
        Person p = new Person();
        p.setFirstName("Maria");
        p.setLastName("González López");
        p.setBirthDay(Instant.now());
        p.setSex(FEMALE);
        assertThat(service.savePerson(p), notNullValue());


        Optional<Person> person = service.findPerson(Example.of(p));
        assertThat(person.isPresent(), is(true));
        assertThat(person.get(), allOf(
                hasProperty("id", greaterThan(0L)),
                hasProperty("firstName", is(p.getFirstName())),
                hasProperty("lastName", is(p.getLastName())),
                hasProperty("birthDay", is(p.getBirthDay())),
                hasProperty("sex", is(FEMALE))
        ));

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("lastName", exact())
                .withMatcher("sex", exact());

        person = service.findPerson(Example.of(p, matcher));
        assertThat(person.isPresent(), is(true));
        assertThat(person.get(), allOf(
                hasProperty("id", greaterThan(0L)),
                hasProperty("firstName", is(p.getFirstName())),
                hasProperty("lastName", is(p.getLastName())),
                hasProperty("birthDay", is(p.getBirthDay())),
                hasProperty("sex", is(FEMALE))
        ));

        p = new Person();
        p.setId(2L);
        person = service.findPerson(Example.of(p));
        assertThat(person.isPresent(), is(false));
    }

    @Test
    public void findPeople() {
        for (int i = 0; i < 10; i++) {
            Person p = new Person();
            p.setFirstName(RandomString.make());
            p.setLastName((i%2==0 ? "F" : "R") + i);
            p.setBirthDay(Instant.ofEpochSecond(1 + (long) (Math.random() * (1542230118 - 1))));
            p.setSex(i%3==0 ? MALE : FEMALE);
            service.savePerson(p);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("sex", exact());

        Person p = new Person();
        p.setLastName("f");

        List<Person> list = service.findPeople(Example.of(p, matcher));
        assertThat(list, hasSize(5));

        p.setSex(MALE);
        list = service.findPeople(Example.of(p, matcher));
        assertThat(list, hasSize(2));
    }

    @Test
    public void saveRelation() {
        Person mother = new Person();
        mother.setFirstName("Maria");
        mother.setLastName("González López");
        mother.setBirthDay(Instant.now());
        mother.setSex(FEMALE);

        Person son = new Person();
        son.setFirstName("Juan");
        son.setLastName("García González");
        son.setBirthDay(Instant.now());
        son.setSex(MALE);

        Relative r = new Relative();
        r.setOrigin(mother);
        r.setDestination(son);
        r.setType(PARENT);

        Relative relative = service.saveRelative(r);

        assertThat(relative, allOf(
                notNullValue(),
                allOf(
                        hasProperty("id", greaterThan(0L)),
                        hasProperty("principal", hasProperty("firstName", is(mother.getFirstName()))),
                        hasProperty("secondary", hasProperty("firstName", is(son.getFirstName()))),
                        hasProperty("type", is(PARENT))
                )
        ));
    }

    @Test
    public void addRelatives() {
        String fname = "bresler-pelaez";
        Family f = service.createFamily(fname);

        Person mother = new Person();
        mother.setFirstName("Maria");
        mother.setLastName("González López");
        mother.setBirthDay(Instant.now());
        mother.setSex(FEMALE);
        mother = service.savePerson(mother);

        Person son = new Person();
        son.setFirstName("Juan");
        son.setLastName("García González");
        son.setBirthDay(Instant.now());
        son.setSex(MALE);
        son = service.savePerson(son);


        Relative r = new Relative();
        r.setOrigin(mother);
        r.setDestination(son);
        r.setType(PARENT);
        r = service.saveRelative(r);

        f = service.addRelatives(f, r);

        assertThat(f.getRelatives(), hasItem(r));

        Optional<Family> check = service.findFamily(Example.of(f, ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
        ));

        assertThat(check.isPresent(), is(true));
        assertThat(check.get().getRelatives(), hasItem(r));
    }
}