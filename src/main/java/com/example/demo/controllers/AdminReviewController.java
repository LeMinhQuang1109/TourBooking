package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.services.ReviewService;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String listReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviews());
        return "admin/reviews";
    }

    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đánh giá thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa đánh giá: " + e.getMessage());
        }
        return "redirect:/admin/reviews";
    }
} 