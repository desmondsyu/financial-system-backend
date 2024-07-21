package com.vitalysukhinin.financial_system.dto;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class UserDetailedResponse {
    private String username;
    private LocalDateTime dob;
    private String email;
    private String mStatus;

    public UserDetailedResponse(String username, LocalDateTime dob, String email, String mStatus) {
        this.username = username;
        this.dob = dob;
        this.email = email;
        this.mStatus = mStatus;
    }

    public UserDetailedResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
