package com.vitalysukhinin.financial_system.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "temp_user")
public class TempUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String token;
    @Column(name = "expires")
    private LocalDateTime expiryDate;
    private String username;
    private String password;
    @Column(name = "Mstatus", length = 50)
    private String mStatus;
    private LocalDateTime dob;

    public TempUser() {}

    public TempUser(String email, String token, LocalDateTime expiryDate, String username, String password, String mStatus, LocalDateTime dob) {
        this.email = email;
        this.token = token;
        this.expiryDate = expiryDate;
        this.username = username;
        this.password = password;
        this.mStatus = mStatus;
        this.dob = dob;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public LocalDateTime getDob() {
        return dob;
    }

    public void setDob(LocalDateTime dob) {
        this.dob = dob;
    }
}
