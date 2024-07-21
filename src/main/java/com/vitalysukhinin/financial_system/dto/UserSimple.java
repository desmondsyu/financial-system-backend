package com.vitalysukhinin.financial_system.dto;

public class UserSimple {
    private String username;

    public UserSimple(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
