package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Admin;
import com.example.demo.Entity.User;
import com.example.demo.Entity.userCreationRequest;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.userCreationRequestRepository;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private userCreationRequestRepository userCreationRequestRepository;
    // ✅ LOGIN METHOD
    public boolean login(String username, String password, String role) {
        if ("admin".equalsIgnoreCase(role)) {
            return adminRepository.findByUsername(username)
                   .map(admin -> admin.getPassword().equals(password))
                   .orElse(false);
        } else {
            return userRepository.findByUsername(username)
                   .map(user -> user.getPassword().equals(password) && user.isAccessApproved())
                   .orElse(false);
        }
    }

    // ✅ SIGN-UP METHOD
    public void signUpUser(String username, String password, String role) {
        if ("admin".equalsIgnoreCase(role)) {
            // Register admin immediately
            if (adminRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Admin username already exists!");
            }
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(password);
            adminRepository.save(admin);
        } else {
            // Check if the username already exists in userCreationRequest table
            if (userCreationRequestRepository.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("User signup request already exists! Await admin approval.");
            }
            
            // Save request for approval
            userCreationRequest request = new userCreationRequest();
            request.setUsername(username);
            request.setPassword(password); // Ensure password is stored correctly
            request.setRole(role);
            request.setApproved(false); // Initially, not approved
            userCreationRequestRepository.save(request);
        }
    }


    // ✅ APPROVE USER - Moves user from request table to actual user table
    public boolean approveUser(String username) {
        Optional<userCreationRequest> requestOpt = userCreationRequestRepository.findByUsernameAndApprovedFalse(username);

        if (requestOpt.isPresent()) {
            userCreationRequest request = requestOpt.get();

            // ✅ Move user from request table to actual user table
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword()); // Keep the same password
            user.setRole(request.getRole());
            user.setAccessApproved(true);

            userRepository.save(user); // ✅ Save to actual user table
            userCreationRequestRepository.delete(request); // ✅ Remove from request table

            return true;
        }
        return false; // If no pending request found
    }

    // ✅ REJECT USER - Deletes request from DB
    public boolean rejectUser(String username) {
        Optional<userCreationRequest> requestOpt = userCreationRequestRepository.findByUsernameAndApprovedFalse(username);

        if (requestOpt.isPresent()) {
            userCreationRequestRepository.delete(requestOpt.get()); // ✅ Simply delete the request
            return true;
        }
        return false; // If no request found
    }
    
    
}


