package com.vitalysukhinin.financial_system.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record UserRequest(String email,
                          String username,
                          String password,
                          String mStatus,
                          LocalDateTime dob) {
}
