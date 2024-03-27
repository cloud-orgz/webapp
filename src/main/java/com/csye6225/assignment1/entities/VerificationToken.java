package com.csye6225.assignment1.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "`verification_tokens`")
public class VerificationToken {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public VerificationToken(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusDays(1); // Token expires in 1 day
    }

    // Default constructor
    public VerificationToken() {
        // This is needed by Hibernate
    }
}

