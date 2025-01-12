package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Tour;
import com.example.demo.models.User;
import com.example.demo.models.UserDto;
import com.example.demo.repositories.TourRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.TourService;
import com.example.demo.services.BookingService;
import com.example.demo.models.Booking;
import com.example.demo.models.Category;
import com.example.demo.services.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private TourRepository tourRepository;
    
    @Autowired
    private TourService tourService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/dashboard")
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

    @GetMapping("/tours")
    public String tourManagement(Model model) {
        List<Tour> tours = tourRepository.findAll();
        model.addAttribute("tours", tours);
        return "admin/tours";
    }

    @GetMapping("/tours/add")
    public String showAddTourForm(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/tour-form";
    }

    @GetMapping("/tours/{id}/edit")
    public String showEditTourForm(@PathVariable Integer id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/tour-form";
    }

    @PostMapping("/tours/save")
    public String saveTour(@ModelAttribute Tour tour, 
                          @RequestParam("categoryId") Integer categoryId) {
        // Set category before saving
        Category category = categoryService.getCategoryById(categoryId);
        tour.setCategory(category);
        tourService.saveTour(tour);
        return "redirect:/admin/tours";
    }

    @PostMapping("/tours/{id}/delete")
    @ResponseBody
    public String deleteTour(@PathVariable Integer id) {
        try {
            tourRepository.deleteById(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
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
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setRole(user.getRole());
        
        model.addAttribute("userDto", userDto);
        model.addAttribute("userId", userId);
        return "admin/edit-user";
    }

    @PostMapping("/users/{userId}/edit")
    public String updateUser(@PathVariable Long userId, 
                           @Valid @ModelAttribute("userDto") UserDto userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            if (userDto.getPassword() == null || userDto.getPassword().trim().isEmpty()) {
                bindingResult.getFieldErrors("password").forEach(error -> 
                    bindingResult.rejectValue("password", error.getCode(), ""));
            }
            if (bindingResult.hasErrors()) {
                return "admin/edit-user";
            }
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

    @GetMapping("/tours/{id}/details")
    public String showTourDetails(@PathVariable Integer id, Model model) {
        Tour tour = tourService.getTourById(id);
        List<Booking> tourBookings = bookingService.getBookingsByTour(tour);
        
        model.addAttribute("tour", tour);
        model.addAttribute("bookings", tourBookings);
        return "admin/tour-details";
    }
}