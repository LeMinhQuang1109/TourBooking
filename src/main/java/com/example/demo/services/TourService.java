package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.Booking;
import com.example.demo.models.Tour;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.TourRepository;

@Service
public class TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private BookingRepository bookingRepository;

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

    @Transactional
    public Tour saveTour(Tour updatedTour) {
        if (updatedTour.getId() != null) {
            // Đây là cập nhật tour đã tồn tại
            Tour existingTour = tourRepository.findById(updatedTour.getId())
                .orElseThrow(() -> new RuntimeException("Tour not found"));
            
            List<Booking> bookings = bookingRepository.findByTourOrderByBookingDateDesc(existingTour);
            
            if (!bookings.isEmpty()) {
                // Nếu tour đã có booking, giữ nguyên maxPeople và availableSlots
                updatedTour.setMaxPeople(existingTour.getMaxPeople());
                updatedTour.setAvailableSlots(existingTour.getAvailableSlots());
            }
            
            // Cập nhật các thông tin khác
            existingTour.setName(updatedTour.getName());
            existingTour.setDescription(updatedTour.getDescription());
            existingTour.setPrice(updatedTour.getPrice());
            existingTour.setLocation(updatedTour.getLocation());
            existingTour.setCategory(updatedTour.getCategory());
            existingTour.setImageUrl(updatedTour.getImageUrl());
            
            // Nếu tour chưa có booking, cho phép cập nhật maxPeople và availableSlots
            if (bookings.isEmpty()) {
                existingTour.setMaxPeople(updatedTour.getMaxPeople());
                existingTour.setAvailableSlots(updatedTour.getAvailableSlots());
            }
            
            return tourRepository.save(existingTour);
        } else {
            // Đây là tạo tour mới
            return tourRepository.save(updatedTour);
        }
    }

    public long countTours() {
        return tourRepository.count();
    }

    @Transactional
    public void deleteTour(Integer id) {
        Tour tour = tourRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tour not found"));
            
        // Kiểm tra xem tour có booking nào không
        List<Booking> bookings = bookingRepository.findByTourOrderByBookingDateDesc(tour);
        if (!bookings.isEmpty()) {
            throw new RuntimeException("Cannot delete tour with existing bookings");
        }
        
        tourRepository.delete(tour);
    }
}