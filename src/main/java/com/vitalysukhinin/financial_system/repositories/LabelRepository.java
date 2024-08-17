package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {
    Optional<Label> findByName(String name);
    List<Label> findByUserOrUserIsNull(User user);
    @Query("SELECT l FROM Label l WHERE l.name = :name AND (l.user = :user OR l.user IS NULL)")
    Optional<Label> findByNameAndUserOrNoUser(@Param("name") String name, @Param("user") User user);
}
