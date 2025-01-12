package com.example.demo.services;

import com.example.demo.models.Tour;
import com.example.demo.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Tour> getAvailableTours() {
        return tourRepository.findByStatus(1);
    }

    public Tour getTourById(Integer id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour not found"));
    }

    public List<Tour> getToursWithAvailableSlots() {
        return tourRepository.findByAvailableSlotsGreaterThan(0);
    }

    public List<Tour> getRecentTours() {
        return tourRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public Tour saveTour(Tour tour) {
        if (tour.getAvailableSlots() != null) {
            tour.setStatus(tour.getAvailableSlots() > 0 ? 1 : 0);
        } else {
            tour.setStatus(1);
        }
        return tourRepository.save(tour);
    }

    public long countTours() {
        return tourRepository.count();
    }
}