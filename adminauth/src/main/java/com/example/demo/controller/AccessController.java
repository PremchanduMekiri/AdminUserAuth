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

    
 // ✅ USER REQUESTS ACCESS FOR A URL
    @PostMapping("/access/request")
    public String requestUrlAccess(@RequestParam String username, @RequestParam String url, RedirectAttributes redirectAttributes) {
        if (username == null || url == null || username.trim().isEmpty() || url.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid request. Username and URL are required.");
            return "redirect:/user";
        }

        accessService.requestAccessForUrl(username, url);
        redirectAttributes.addFlashAttribute("message", "Access request sent. Await admin approval.");
        return "redirect:/user";
    }

    @PostMapping("/admin/approveAccess/{id}")
    public String approveAccess(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AccessRequest request = accessRequestRepository.findById(id).orElse(null);

        if (request != null) {
            request.setApproved(true);

            int expiryMinutes =2; // Token valid for 2 minutes
            // ✅ Get the requested URL from the request entity (you must be storing this in DB earlier)
            String requestedUrl = request.getUrl();  // make sure this field exists and is saved when requesting
            
            String token = jwtTokenUtil.generateToken(request.getUsername(), requestedUrl, expiryMinutes);

            request.setToken(token);  // ✅ Save token to DB
            request.setExpiryTime(LocalDateTime.now().plusMinutes(expiryMinutes)); // ✅ Set expiry time in DB

            accessRequestRepository.save(request); // ✅ Persist updates

            redirectAttributes.addFlashAttribute("message", "Approved access for " + request.getUsername());
        } else {
            redirectAttributes.addFlashAttribute("error", "Access request not found.");
        }

        return "redirect:/admin"; // Stay on admin dashboard
    }


    @PostMapping("/admin/rejectAccess")
    public String rejectAccess(@RequestParam String username, @RequestParam String url, RedirectAttributes redirectAttributes) {
        boolean rejected = accessService.rejectAccess(username, url);

        if (rejected) {
            redirectAttributes.addFlashAttribute("message", "Access rejected for " + username);
        } else {
            redirectAttributes.addFlashAttribute("error", "Access rejection failed.");
        }

        return "redirect:/admin";
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


