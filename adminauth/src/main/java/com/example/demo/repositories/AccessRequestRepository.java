package com.example.demo.repositories;

import com.example.demo.Entity.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
    Optional<AccessRequest> findByUsername(String username);

    List<AccessRequest> findByApprovedFalse(); // Find pending requests

    List<AccessRequest> findByApprovedTrue(); // Find approved requests
    Optional<AccessRequest> findByUsernameAndUrl(String username, String url);
    List<AccessRequest> findByUrl(String url); 


}
