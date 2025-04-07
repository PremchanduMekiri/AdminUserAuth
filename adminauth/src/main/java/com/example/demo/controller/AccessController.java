package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Entity.AccessRequest;
import com.example.demo.Entity.User;
import com.example.demo.JwtToken.JwtTokenUtil;
import com.example.demo.repositories.AccessRequestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.userCreationRequestRepository;
import com.example.demo.service.AccessService;

@Controller
public class AccessController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccessService accessService;

    @Autowired
    private AccessRequestRepository accessRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private userCreationRequestRepository userCreationRequestRepository;

    // ✅ USER REQUESTS ACCESS
    @PostMapping("/access/request")
    public String requestAccess(@RequestParam String username, RedirectAttributes redirectAttributes) {
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid request. Username is required.");
            return "redirect:/user";
        }

        accessService.requestAccess(username);
        redirectAttributes.addFlashAttribute("message", "Request Sent. Await Admin Approval.");
        return "redirect:/user";
    }

    @PostMapping("/admin/approveAccess/{id}")
    public String approveAccess(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AccessRequest request = accessRequestRepository.findById(id).orElse(null);

        if (request != null) {
            request.setApproved(true);

            int expiryMinutes =2; // Token valid for 2 minutes
            String token = jwtTokenUtil.generateToken(request.getUsername(), expiryMinutes);

            request.setToken(token);  // ✅ Save token to DB
            request.setExpiryTime(LocalDateTime.now().plusMinutes(expiryMinutes)); // ✅ Set expiry time in DB

            accessRequestRepository.save(request); // ✅ Persist updates

            redirectAttributes.addFlashAttribute("message", "Approved access for " + request.getUsername());
        } else {
            redirectAttributes.addFlashAttribute("error", "Access request not found.");
        }

        return "redirect:/admin"; // Stay on admin dashboard
    }



    // ✅ ADMIN REJECTS ACCESS
    @PostMapping("/admin/rejectAccess")
    public String rejectAccess(@RequestParam String username, RedirectAttributes redirectAttributes) {
        accessService.rejectAccess(username);
        redirectAttributes.addFlashAttribute("message", "Access Rejected for " + username);
        return "redirect:/admin";
    }

    // ✅ USER VIEWS USERS IF ACCESS APPROVED
    @GetMapping("/access/view-users")
    public String viewApprovedUsers(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
        if (!jwtTokenUtil.validateToken(token)) {
            redirectAttributes.addFlashAttribute("error", "Token expired or invalid.");
            return "redirect:/user";
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        Optional<AccessRequest> accessRequest = accessRequestRepository.findByUsername(username);

        if (accessRequest.isPresent() && accessRequest.get().isApproved()) {
            List<User> users = userRepository.findAll();
            model.addAttribute("users", users);
            model.addAttribute("username", username);
            return "users";
        } else {
            redirectAttributes.addFlashAttribute("error", "Access denied. Not approved.");
            return "redirect:/user";
        }
    }

    // ✅ ADMIN SEES ALL PENDING REQUESTS
    @GetMapping("/pending")
    public String pendingRequests(Model model) {
        List<AccessRequest> requests = accessService.getPendingRequests();
        if (requests == null || requests.isEmpty()) {
            model.addAttribute("error", "No pending requests at the moment.");
        } else {
            model.addAttribute("requests", requests);
        }
        return "admin";
    }
}


