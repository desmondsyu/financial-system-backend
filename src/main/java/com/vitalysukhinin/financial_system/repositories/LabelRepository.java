package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {
    Optional<Label> findByName(String name);
    List<Label> findByUserOrUserIsNull(User user);
}
