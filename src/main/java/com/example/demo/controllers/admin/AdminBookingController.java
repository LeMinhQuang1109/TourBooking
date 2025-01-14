package com.example.demo.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.services.BookingService;

@Controller
@RequestMapping("/admin/bookings")
public class AdminBookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }

    @PostMapping("/{id}/confirm")
    public String confirmBooking(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.confirmBooking(id);
            redirectAttributes.addFlashAttribute("successMessage", "Booking confirmed successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error confirming booking: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }

    @PostMapping("/{id}/reject")
    public String rejectBooking(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.rejectBooking(id);
            redirectAttributes.addFlashAttribute("successMessage", "Booking rejected successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error rejecting booking: " + e.getMessage());
        }
        return "redirect:/admin/bookings";
    }
} 