package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.TempResetUser;
import com.vitalysukhinin.financial_system.entities.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempResetUserRepository extends JpaRepository<TempResetUser, Integer> {
    Optional<TempResetUser> findByToken(String token);
}
