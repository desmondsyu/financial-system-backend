package com.vitalysukhinin.financial_system.repositories;

import com.vitalysukhinin.financial_system.entities.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TempUserRepository extends JpaRepository<TempUser, Integer> {
    Optional<TempUser> findByEmailAndToken(String email, String token);
    Optional<TempUser> findByEmail(String email);
}
