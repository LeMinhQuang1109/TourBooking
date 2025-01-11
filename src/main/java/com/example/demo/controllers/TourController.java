package com.example.demo.controllers;

import com.example.demo.models.Tour;
import com.example.demo.models.User;
import com.example.demo.repositories.TourRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "*")
public class TourController {

    @Autowired
    private TourRepository tourRepository;

    // Public: Xem danh sách tour
    @GetMapping
    public ResponseEntity<List<Tour>> getAllTours() {
        List<Tour> tours = tourRepository.findAll();
        return ResponseEntity.ok(tours);
    }

    // Public: Xem chi tiết tour
    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable int id) {
        return tourRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin: Tạo tour mới
    @PostMapping
    public ResponseEntity<?> createTour(@Valid @RequestBody Tour tour) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        tour.setCreatedAt(LocalDateTime.now());
        Tour savedTour = tourRepository.save(tour);
        return ResponseEntity.ok(savedTour);
    }

    // Admin: Cập nhật tour
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTour(@PathVariable int id, @Valid @RequestBody Tour tourDetails) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return tourRepository.findById(id)
                .map(tour -> {
                    tour.setName(tourDetails.getName());
                    tour.setDescription(tourDetails.getDescription());
                    tour.setPrice(tourDetails.getPrice());
                    tour.setDuration(tourDetails.getDuration());
                    tour.setLocation(tourDetails.getLocation());
                    // Không cập nhật createdAt
                    Tour updatedTour = tourRepository.save(tour);
                    return ResponseEntity.ok(updatedTour);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin: Xóa tour
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTour(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return tourRepository.findById(id)
                .map(tour -> {
                    tourRepository.delete(tour);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Tour deleted successfully");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // User & Admin: Tìm kiếm tour theo location
    @GetMapping("/search")
    public ResponseEntity<List<Tour>> searchTours(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        
        List<Tour> tours = tourRepository.findAll();
        
        // Lọc theo các tiêu chí
        if (location != null && !location.isEmpty()) {
            tours = tours.stream()
                    .filter(tour -> tour.getLocation().toLowerCase().contains(location.toLowerCase()))
                    .toList();
        }
        
        if (minPrice != null) {
            tours = tours.stream()
                    .filter(tour -> tour.getPrice() >= minPrice)
                    .toList();
        }
        
        if (maxPrice != null) {
            tours = tours.stream()
                    .filter(tour -> tour.getPrice() <= maxPrice)
                    .toList();
        }
        
        return ResponseEntity.ok(tours);
    }
}
