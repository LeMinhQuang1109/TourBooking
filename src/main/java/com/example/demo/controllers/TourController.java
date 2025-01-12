package com.example.demo.controllers;

import com.example.demo.models.Tour;
import com.example.demo.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tours")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping
    public String listTours(Model model) {
        List<Tour> availableTours = tourService.getAvailableTours();
        model.addAttribute("tours", availableTours);
        return "tour/list";
    }

    @GetMapping("/{id}")
    public String tourDetail(@PathVariable Integer id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        return "tour/detail";
    }
}