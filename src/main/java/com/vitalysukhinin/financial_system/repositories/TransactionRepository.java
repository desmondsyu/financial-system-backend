package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.Label;
import com.vitalysukhinin.financial_system.entities.Transaction;
import com.vitalysukhinin.financial_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

}
