package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.UserDto;
import com.example.demo.services.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserDto userDto, 
                             BindingResult result, 
                             Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        if (userService.existsByUsername(userDto.getUsername())) {
            model.addAttribute("usernameError", "Username already exists");
            return "auth/register";
        }

        if (userService.existsByEmail(userDto.getEmail())) {
            model.addAttribute("emailError", "Email already exists");
            return "auth/register";
        }

        userService.registerUser(userDto);
        return "redirect:/login?registered";
    }
} 