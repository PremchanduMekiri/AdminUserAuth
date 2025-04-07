package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Entity.AccessRequest;
import com.example.demo.Entity.User;
import com.example.demo.Entity.userCreationRequest;
import com.example.demo.repositories.AccessRequestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.userCreationRequestRepository;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller

public class MainController {
    
    @Autowired
    private AuthService authService;
    @Autowired
    private userCreationRequestRepository userCreationRequestRepository;
    @Autowired
    private AccessRequestRepository accessRequestRepository;
    
    @Autowired
    private UserRepository userrepository;
    

    @GetMapping("/")
    public String loginPage() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        @RequestParam String role, 
                        RedirectAttributes redirectAttributes,
                        HttpSession session) {  // ✅ Add session parameter
        boolean isAuthenticated = authService.login(username, password, role);

        if (isAuthenticated) {
            // ✅ Store username in session for later use
            session.setAttribute("username", username);

            if ("admin".equalsIgnoreCase(role)) {
                redirectAttributes.addFlashAttribute("success", "Admin login successful!");
                return "redirect:/admin";
            } else {
                redirectAttributes.addFlashAttribute("success", "User login successful!");
                return "redirect:/user";
            }
        }

        // If login fails
        redirectAttributes.addFlashAttribute("error", "Invalid credentials or account not approved.");
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        // Get pending user creation requests
        List<userCreationRequest> userRequests = userCreationRequestRepository.findAll();

        // Get pending access requests (approved = false)
        List<AccessRequest> accessRequests = accessRequestRepository.findByApprovedFalse();

        model.addAttribute("userRequests", userRequests);
        model.addAttribute("accessRequests", accessRequests);

        return "admin"; // Return to the admin.jsp dashboard
    }


    @GetMapping("/user")
    public String userDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username); // Greeting on the page

        Optional<AccessRequest> optionalRequest = accessRequestRepository.findByUsername(username);

        if (optionalRequest.isPresent()) {
            AccessRequest request = optionalRequest.get();

            if (request.isApproved()) {
                if (request.getExpiryTime() != null && request.getExpiryTime().isAfter(LocalDateTime.now())) {
                    model.addAttribute("token", request.getToken());
                    model.addAttribute("message", "Access approved!");
                    model.addAttribute("isApproved", true); 
                    model.addAttribute("expiryTime", request.getExpiryTime().toString());

                } else {
                    request.setApproved(false);
                    request.setToken(null);
                    request.setExpiryTime(null);
                    accessRequestRepository.save(request);

                    model.addAttribute("error", "Your token has expired.");
                    model.addAttribute("isApproved", false); // ✅ Optional but good to have
                }
            } else {
                model.addAttribute("error", "Your request is still pending.");
                model.addAttribute("isApproved", false);
            }
        } else {
            model.addAttribute("error", "No access request found.");
            model.addAttribute("isApproved", false);
        }

        return "user";
    }





    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, 
                         @RequestParam String role, RedirectAttributes redirectAttributes) {
        if ("admin".equalsIgnoreCase(role)) {
            authService.signUpUser(username, password,role); // ✅ Fixed method call
            redirectAttributes.addFlashAttribute("success", "Admin signup successful! You can now log in.");
        } else {
            authService.signUpUser(username, password, role); // ✅ Regular user signup requires appr
            
            redirectAttributes.addFlashAttribute("success", "Signup successful! Await admin approval.");
        }
        return "redirect:/";
    }

    @PostMapping("/admin/rejectUser")
    public String rejectUser(@RequestParam String username, RedirectAttributes redirectAttributes) {
        // 🔹 Debugging log to ensure method is called
        System.out.println("🚨 Rejecting user: " + username);

        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid username for rejection.");
            return "redirect:/admin";  // Prevent null username errors
        }

        boolean rejected = authService.rejectUser(username);

        if (rejected) {
            System.out.println("✅ User rejected successfully: " + username);
            redirectAttributes.addFlashAttribute("success", "User request rejected successfully!");
        } else {
            System.out.println("❌ User rejection failed: " + username);
            redirectAttributes.addFlashAttribute("error", "User rejection failed. User not found.");
        }

        return "redirect:/admin"; // Redirect back to admin dashboard
    }

@PostMapping("/admin/approveUser")  // Ensure this matches exactly
public String approveUser(@RequestParam String username, RedirectAttributes redirectAttributes) {
    System.out.println("🚀 Approving user: " + username);

    boolean approved = authService.approveUser(username);

    if (approved) {
        System.out.println("✅ User approved successfully: " + username);
        redirectAttributes.addFlashAttribute("success", "User approved successfully!");
    } else {
        System.out.println("❌ User approval failed: " + username);
        redirectAttributes.addFlashAttribute("error", "User approval failed. User not found.");
    }

    return "redirect:/admin";  // Ensure it redirects properly
}
@GetMapping("/admin/users")
public String viewAllUsers(RedirectAttributes model) {
    System.out.println("🚀 Fetching all users...");

    // Fetch all users directly (Admins have full access)
    List<User> users = userrepository.findAll();
     model.addFlashAttribute("users", users); // ✅ Send users list to JSP
    

    return "redirect:/users2"; // ✅ Return JSP directly, NO redirect
}


@GetMapping("users2")
public String users() { 
    return "admin-users";
}

@GetMapping("/user/dashboard")
public String userDashboard() {
    // You can add any logic or model attributes here
    return "redirect:/user";  // The name of your dashboard JSP view
}

@GetMapping("/logout")
public String logout() {

    return "redirect:/";  // Redirect to login page after logout
}





}

