package com.example.demo.repositories;

import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByAccessApproved(boolean approved);
    Optional<User> findByUsername(String username);
}

