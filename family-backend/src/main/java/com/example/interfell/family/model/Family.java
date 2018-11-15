package com.example.interfell.family.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    @OneToMany
    private Set<Relative> relatives;

    public Family() {
        this.relatives = new HashSet<>();
    }

    public Family(String name) {
        this.name = name;
        this.relatives = new HashSet<>();
    }

    public void addRelative(Relative relative) {
        if (Objects.isNull(this.relatives))
            this.relatives = new HashSet<>();
        this.relatives.add(relative);
    }
}
