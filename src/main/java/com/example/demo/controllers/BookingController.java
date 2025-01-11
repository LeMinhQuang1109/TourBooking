package com.example.demo.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BookingRequest;
import com.example.demo.models.Booking;
import com.example.demo.models.User;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.TourRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;

    // User: Đặt tour
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        return tourRepository.findById(request.getTourId())
            .map(tour -> {
                double totalPrice = tour.getPrice() * request.getNumberOfPeople();

                Booking booking = new Booking();
                booking.setUser(currentUser);
                booking.setTour(tour);
                booking.setBookingDate(LocalDateTime.now());
                booking.setNumberOfPeople(request.getNumberOfPeople());
                booking.setTotalPrice(totalPrice);
                booking.setContactPhone(request.getContactPhone());

                Booking savedBooking = bookingRepository.save(booking);
                return ResponseEntity.ok(savedBooking);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // User: Xem booking của mình
    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        List<Booking> bookings = bookingRepository.findByUserId(currentUser.getId());
        return ResponseEntity.ok(bookings);
    }

    // Admin: Xem tất cả bookings
    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }
        
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok(bookings);
    }

    // Admin: Cập nhật trạng thái booking
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return bookingRepository.findById(id)
            .map(booking -> {
                booking.setStatus(status);
                Booking updatedBooking = bookingRepository.save(booking);
                return ResponseEntity.ok(updatedBooking);
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 