package com.example.demo.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.services.UserService;
import com.example.demo.services.TourService;
import com.example.demo.services.BookingService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private TourService tourService;
    
    @Autowired
    private BookingService bookingService;

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        // Lấy tổng số liệu
        long totalUsers = userService.countUsers();
        long totalTours = tourService.countTours();
        long totalBookings = bookingService.countBookings();
        
        // Tính tổng doanh thu
        double totalRevenue = bookingService.calculateTotalRevenue();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalTours", totalTours);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalRevenue", totalRevenue);
        
        return "admin/dashboard";
    }
} 