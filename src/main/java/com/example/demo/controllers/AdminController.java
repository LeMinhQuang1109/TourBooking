package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Thống kê cơ bản
        long totalUsers = userService.getUserCount();
        List<User> recentUsers = userService.getRecentUsers();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("recentUsers", recentUsers);
        return "admin/dashboard";
    }

    @GetMapping("")
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/users")
    public String userManagement(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/users/{userId}/toggle-status")
    @ResponseBody
    public String toggleUserStatus(@PathVariable Long userId) {
        try {
            userService.toggleUserStatus(userId);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @PostMapping("/users/{userId}/change-role")
    @ResponseBody
    public String changeUserRole(@PathVariable Long userId, @RequestParam String newRole) {
        try {
            userService.changeUserRole(userId, newRole);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/users/{userId}/edit")
    public String updateUser(@PathVariable Long userId, 
                           @ModelAttribute User user,
                           RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(userId, user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{userId}/delete")
    @ResponseBody
    public String deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}