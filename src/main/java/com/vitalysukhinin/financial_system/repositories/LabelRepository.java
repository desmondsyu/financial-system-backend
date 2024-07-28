package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Integer> {
    public Optional<Label> findByName(String name);
}
