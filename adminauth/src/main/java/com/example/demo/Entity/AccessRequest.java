package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private boolean approved = false; // Default: Not approved

    @Lob  // Optional: if you expect long token strings
    private String token;

    private LocalDateTime expiryTime;

    // Constructors
    public AccessRequest() {}

    public AccessRequest(String username) {
        this.username = username;
        this.approved = false;
    }
}


