package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.TransactionGroup;
import com.vitalysukhinin.financial_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionGroupRepository extends JpaRepository<TransactionGroup, Integer> {
    List<TransactionGroup> findAllByUser(User user);
    Optional<TransactionGroup> findByNameAndUser(String name, User user);
}
