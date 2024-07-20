package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
