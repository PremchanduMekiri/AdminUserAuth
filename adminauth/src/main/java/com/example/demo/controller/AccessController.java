package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Entity.AccessRequest;
import com.example.demo.Entity.User;
import com.example.demo.Entity.userCreationRequest;
import com.example.demo.repositories.AccessRequestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.userCreationRequestRepository;
import com.example.demo.service.AccessService;

@Controller

public class AccessController {

    @Autowired
    private AccessService accessService;
    
    @Autowired
    private AccessRequestRepository accessRequestRepository;
    
    @Autowired
    private UserRepository userrepository;
    
    @Autowired
    private userCreationRequestRepository userCreationRequestRepository;

    @PostMapping("/access/request")
    public String requestAccess(@RequestParam String username, RedirectAttributes redirectAttributes) {
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid request. Username is required.");
            return "redirect:/user";
        }

        accessService.requestAccess(username);
        redirectAttributes.addFlashAttribute("message", "Request Sent. Await Admin Approval.");
        
        return "redirect:/user"; // ✅ Redirect back to user dashboard
    }

    
    @PostMapping("/admin/approveAccess")
    public String approveAccess(@RequestParam String username, RedirectAttributes redirectAttributes) {
        Optional<AccessRequest> optionalRequest = accessRequestRepository.findByUsername(username);
        
        if (optionalRequest.isPresent()) {
            AccessRequest request = optionalRequest.get();
            request.setApproved(true); // ✅ Set approval to true
            accessRequestRepository.save(request); // ✅ Save the updated entity

            redirectAttributes.addFlashAttribute("success", "Access request approved!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User request not found!");
        }
        
        return "redirect:/admin";
    }



    // ✅ ADMIN REJECTS REQUEST
    @PostMapping("/admin/rejectAccess")
    public String rejectAccess(@RequestParam String username) {
        accessService.rejectAccess(username);
        return "redirect:/admin?message=Access Rejected.";
    }

  
    @GetMapping("/access/view-users")
    public String viewApprovedUsers(@RequestParam String username, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("🚀 Request received to view users for: " + username);

        if (accessRequestRepository == null || userrepository == null) {
            System.out.println("❌ ERROR: Repositories are NULL!");
            redirectAttributes.addFlashAttribute("error", "Internal error: Repository not initialized.");
            return "redirect:/user";
        }

        // Check if the user is approved by admin
        Optional<AccessRequest> accessRequest = accessRequestRepository.findByUsername(username);

        if (accessRequest.isPresent() && accessRequest.get().isApproved()) {
            System.out.println("✅ User is approved, fetching all users...");

            // Fetch all users from the actual user table
            List<User> users = userrepository.findAll();

            if (users == null || users.isEmpty()) {
                System.out.println("⚠️ No users found in the system.");
                redirectAttributes.addFlashAttribute("error", "No users found.");
                return "redirect:/user";
            }

            System.out.println("✅ Users fetched successfully: " + users.size());
            model.addAttribute("users", users);
            model.addAttribute("username", username);
            return "users";  // ✅ Redirect to users.jsp to display user list
        } else {
            System.out.println("❌ Access denied for: " + username);
            redirectAttributes.addFlashAttribute("error", "Access denied! Admin has not approved your request.");
            return "redirect:/user";
        }
    }

   





    // ✅ ADMIN SEES PENDING REQUESTS
    @GetMapping("/pending")
    public String pendingRequests(Model model) {
        List<AccessRequest> requests = accessService.getPendingRequests();
        model.addAttribute("requests", requests);
        return "admin"; // ✅ Show pending requests on admin dashboard
    }
}

