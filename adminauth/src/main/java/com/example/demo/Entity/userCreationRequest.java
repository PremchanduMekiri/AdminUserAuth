package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class userCreationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String role;
    
    
    @Column(nullable = false)
    private boolean approved; 
}

