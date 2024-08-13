package com.vitalysukhinin.financial_system.dto;

import java.time.LocalDateTime;

public record UserRequest(String email,
                          String username,
                          String password,
                          String gender,
                          LocalDateTime dob) {
}
