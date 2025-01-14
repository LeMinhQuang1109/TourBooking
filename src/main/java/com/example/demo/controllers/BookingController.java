package com.example.demo.controllers;

import com.example.demo.models.Booking;
import com.example.demo.models.Tour;
import com.example.demo.models.User;
import com.example.demo.services.BookingService;
import com.example.demo.services.TourService;
import com.example.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TourService tourService;

    @Autowired
    private UserService userService;

    @GetMapping("/tour/{tourId}")
    public String showBookingForm(@PathVariable Integer tourId, Model model) {
        Tour tour = tourService.getTourById(tourId);
        if (tour == null || tour.getStatus() == 0) {
            return "redirect:/tours?error=Tour not available";
        }
        model.addAttribute("tour", tour);
        return "booking/booking-form";
    }

    @PostMapping("/tour/{tourId}")
    public String createBooking(@PathVariable Integer tourId,
                              @RequestParam("numberOfPeople") int numberOfPeople,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            Tour tour = tourService.getTourById(tourId);
            User user = userService.findByUsername(userDetails.getUsername());
            
            bookingService.createBooking(user, tour, numberOfPeople);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Booking created successfully!");
            return "redirect:/bookings/my-bookings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error creating booking: " + e.getMessage());
            return "redirect:/bookings/tour/" + tourId;
        }
    }

    @GetMapping("/my-bookings")
    public String showMyBookings(@AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        List<Booking> bookings = bookingService.getUserBookings(user);
        model.addAttribute("bookings", bookings);
        return "booking/my-bookings";
    }

    @PostMapping("/{bookingId}/cancel")
    public String cancelBooking(@PathVariable Integer bookingId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            bookingService.cancelBooking(bookingId, user);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Booking cancelled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error cancelling booking: " + e.getMessage());
        }
        return "redirect:/bookings/my-bookings";
    }
} 