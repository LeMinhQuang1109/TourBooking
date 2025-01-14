package com.example.demo.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.User;
import com.example.demo.models.UserDto;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String userManagement(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @PostMapping("/{userId}/change-role")
    @ResponseBody
    public String changeUserRole(@PathVariable Integer userId, @RequestParam String newRole) {
        try {
            userService.changeUserRole(userId, newRole);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/{userId}/edit")
    public String editUserForm(@PathVariable Integer userId, Model model) {
        User user = userService.findById(userId);
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setRole(user.getRole());
        
        model.addAttribute("userDto", userDto);
        model.addAttribute("userId", userId);
        return "admin/edit-user";
    }

    @PostMapping("/{userId}/edit")
    public String updateUser(@PathVariable Integer userId, 
                           @Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/edit-user";
        }

        try {
            User user = userService.findById(userId);
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setFullName(userDto.getFullName());
            user.setRole(userDto.getRole());
            
            String newPassword = userDto.getPassword();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (newPassword.length() < 6) {
                    bindingResult.rejectValue("password", "Size", "Password must be at least 6 characters long");
                    return "admin/edit-user";
                }
                user.setPassword(newPassword);
            }
            
            userService.updateUser(userId, user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/{userId}/delete")
    @ResponseBody
    public String deleteUser(@PathVariable Integer userId) {
        try {
            userService.deleteUser(userId);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
} 