package com.example.demo.service;

import com.example.demo.Entity.AccessRequest;
import com.example.demo.Entity.User;
import com.example.demo.repositories.AccessRequestRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccessService {

    @Autowired
    private AccessRequestRepository accessRequestRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ USER SENDS REQUEST TO SEE USERS
//    public void requestAccess(String username) {
//        Optional<AccessRequest> existingRequest = accessRequestRepository.findByUsername(username);
//
//        if (existingRequest.isEmpty()) { // Prevent duplicate requests
//            AccessRequest request = new AccessRequest(username);
//            accessRequestRepository.save(request);
//        }
//    }
    public void requestAccessForUrl(String username, String url) {
        Optional<AccessRequest> existing = accessRequestRepository.findByUsernameAndUrl(username, url);

        if (existing.isEmpty()) {
            AccessRequest request = new AccessRequest(username, url);
            accessRequestRepository.save(request);
        }
    }

    // ✅ ADMIN APPROVES ACCESS REQUEST
    public boolean approveAccess(String username,String url) {
        Optional<AccessRequest> requestOpt = accessRequestRepository.findByUsernameAndUrl(username,url);

        if (requestOpt.isPresent()) {
            AccessRequest request = requestOpt.get();
            request.setApproved(true);
            accessRequestRepository.save(request);
            return true;
        }
        return false;
    }

    // ✅ ADMIN REJECTS ACCESS REQUEST
    public boolean rejectAccess(String username,String url) {
        Optional<AccessRequest> requestOpt = accessRequestRepository.findByUsernameAndUrl(username,url);

        if (requestOpt.isPresent()) {
            accessRequestRepository.delete(requestOpt.get());
            return true;
        }
        return false;
    }

    // ✅ GET ALL APPROVED USERS
    public List<User> getAllUsers(String username) {
        Optional<AccessRequest> request = accessRequestRepository.findByUsername(username);

        if (request.isPresent() && request.get().isApproved()) {
            return userRepository.findAll(); // ✅ Return all users if request is approved
        }
        return null; // ❌ User is not approved
    }

    // ✅ GET PENDING REQUESTS FOR ADMIN
    public List<AccessRequest> getPendingRequests() {
        return accessRequestRepository.findByApprovedFalse();
    }
}


