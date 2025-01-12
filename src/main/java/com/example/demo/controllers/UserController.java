package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.models.UserProfileDto;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewProfile(Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        return "users/profile";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        User user = getCurrentUser();
        if (!model.containsAttribute("userProfileDto")) {
            UserProfileDto profileDto = new UserProfileDto();
            profileDto.setFullName(user.getFullName());
            profileDto.setEmail(user.getEmail());
            model.addAttribute("userProfileDto", profileDto);
        }
        return "users/edit-profile";
    }

    @PostMapping("/update")
    public String updateProfile(@Valid @ModelAttribute UserProfileDto userProfileDto,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfileDto", result);
            redirectAttributes.addFlashAttribute("userProfileDto", userProfileDto);
            return "redirect:/profile/edit";
        }

        try {
            userService.updateProfile(getCurrentUser().getUsername(), userProfileDto);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/profile/edit";
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(auth.getName());
    }
} 