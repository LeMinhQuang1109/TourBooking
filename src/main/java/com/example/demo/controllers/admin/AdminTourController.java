package com.example.demo.controllers.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Booking;
import com.example.demo.models.Category;
import com.example.demo.models.Tour;
import com.example.demo.services.BookingService;
import com.example.demo.services.CategoryService;
import com.example.demo.services.TourService;

@Controller
@RequestMapping("/admin/tours")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTourController {
    
    @Autowired
    private TourService tourService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private BookingService bookingService;

    @GetMapping("")
    public String tourManagement(Model model) {
        List<Tour> tours = tourService.getAllTours();
        model.addAttribute("tours", tours);
        return "admin/tours";
    }

    @GetMapping("/add")
    public String showAddTourForm(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/tour-form";
    }

    @GetMapping("/{id}/edit")
    public String showEditTourForm(@PathVariable Integer id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/tour-form";
    }

    @PostMapping("/save")
    public String saveTour(@ModelAttribute Tour tour, 
                          @RequestParam("categoryId") Integer categoryId,
                          RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            tour.setCategory(category);
            
            Tour savedTour = tourService.saveTour(tour);
            
            if (savedTour.getId() != null && 
                !bookingService.getBookingsByTour(savedTour).isEmpty() && 
                (tour.getMaxPeople() != savedTour.getMaxPeople() || 
                 tour.getAvailableSlots() != savedTour.getAvailableSlots())) {
                    
                redirectAttributes.addFlashAttribute("warningMessage", 
                    "Tour đã có người đặt. Số lượng người tối đa và số chỗ trống không thể thay đổi.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Lưu tour thành công!");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi lưu tour: " + e.getMessage());
        }
        return "redirect:/admin/tours";
    }

    @PostMapping("/{id}/delete")
    @ResponseBody
    public String deleteTour(@PathVariable Integer id) {
        try {
            tourService.deleteTour(id);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/{id}/details")
    public String showTourDetails(@PathVariable Integer id, Model model) {
        Tour tour = tourService.getTourById(id);
        List<Booking> tourBookings = bookingService.getBookingsByTour(tour);
        
        model.addAttribute("tour", tour);
        model.addAttribute("bookings", tourBookings);
        return "admin/tour-details";
    }
} 