package com.example.interfell.family.service;

import com.example.interfell.family.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {
}
