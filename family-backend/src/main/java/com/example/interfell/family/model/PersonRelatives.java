package com.example.interfell.family.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class PersonRelatives {

    private Map<RelType, Set<Person>> relatedWith;
    private Map<RelType, Set<Person>> relativeOf;
    private Map<RelType, Set<Person>> inverseRelativeOf;
}
