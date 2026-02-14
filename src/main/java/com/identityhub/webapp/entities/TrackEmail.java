package com.identityhub.webapp.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`track_email`")
public class TrackEmail {
    @Id
    private String id;

    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime sentAt;
}
