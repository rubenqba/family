package com.example.interfell.family.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "rels")
@EqualsAndHashCode(of = {"origin", "destination"})
public class Relative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Person origin;
    @OneToOne
    private Person destination;
    private RelType type;
}
