package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByHashcode(String hashcode);
}
