package com.example.demo.controllers;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.Category;
import com.example.demo.models.Tour;
import com.example.demo.services.CategoryService;
import com.example.demo.services.TourService;

@Controller
@RequestMapping("/tours")
public class TourController {

    @Autowired
    private TourService tourService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listTours(Model model) {
        List<Tour> tours = tourService.getAvailableTours();
        List<Category> categories = categoryService.getAllCategories();
        
        // Tạo Map để nhóm tours theo category
        Map<Integer, List<Tour>> toursByCategory = new HashMap<>();
        for (Tour tour : tours) {
            if (tour.getCategory() != null) {
                toursByCategory.computeIfAbsent(tour.getCategory().getId(), k -> new ArrayList<>())
                             .add(tour);
            }
        }
        
        model.addAttribute("tours", tours);
        model.addAttribute("categories", categories);
        model.addAttribute("toursByCategory", toursByCategory);
        return "tour/list";
    }

    @GetMapping("/{id}")
    public String tourDetail(@PathVariable Integer id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        return "tour/detail";
    }
}