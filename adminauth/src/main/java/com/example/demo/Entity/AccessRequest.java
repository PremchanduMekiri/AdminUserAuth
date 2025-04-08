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
    
    private String url;

    private boolean approved = false; // Default: Not approved

    @Lob  // Optional: if you expect long token strings
    private String token;

    private LocalDateTime expiryTime;

    // Constructors
    public AccessRequest() {}

    public AccessRequest(String username,String url) {
        this.username = username;
        this.url = url;
        this.approved = false;
    }
}


