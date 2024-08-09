package com.vitalysukhinin.financial_system.dto;

public class UserSimple {
    private String email;

    public UserSimple(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
