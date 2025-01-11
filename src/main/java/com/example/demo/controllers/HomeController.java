package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";  // Trả về trang index.html
    }

    @GetMapping("/home")
    public String homePage() {
        return "index";
    }
} 