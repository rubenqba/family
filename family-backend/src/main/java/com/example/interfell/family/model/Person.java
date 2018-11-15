package com.example.interfell.family.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "people")
@EqualsAndHashCode(of = {"id"})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private Instant birthDay;

    private Sex sex;
}
