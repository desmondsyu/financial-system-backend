package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Integer> {

}
