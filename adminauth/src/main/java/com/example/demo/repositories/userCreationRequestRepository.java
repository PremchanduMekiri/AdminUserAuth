package com.example.demo.repositories;

import com.example.demo.Entity.AccessRequest;
import com.example.demo.Entity.userCreationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface userCreationRequestRepository extends JpaRepository<userCreationRequest, Long> {
     Optional<userCreationRequest> findByUsername(String username);
    Optional<userCreationRequest> findByUsernameAndApprovedFalse(String username);
    List<userCreationRequest> findByApprovedFalse(); 
    
    @Query("SELECT u FROM userCreationRequest u WHERE u.approved = true")
    List<userCreationRequest> findByApprovedTrue();
  
}

