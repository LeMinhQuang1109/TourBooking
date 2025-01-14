package com.example.demo.services;

import com.example.demo.models.Booking;
import com.example.demo.models.Tour;
import com.example.demo.models.User;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TourRepository tourRepository;

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    @Transactional
    public Booking createBooking(User user, Tour tour, int numberOfPeople) {
        // Kiểm tra số lượng chỗ trống
        if (tour.getAvailableSlots() < numberOfPeople) {
            throw new RuntimeException("Not enough available slots");
        }

        // Tạo booking mới
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTour(tour);
        booking.setNumberOfPeople(numberOfPeople);
        booking.setBookingDate(LocalDateTime.now());
        booking.setTotalPrice(tour.getPrice() * numberOfPeople);
        booking.setStatus(0); // Pending

        // Cập nhật số chỗ trống của tour
        tour.setAvailableSlots(tour.getAvailableSlots() - numberOfPeople);
        if (tour.getAvailableSlots() == 0) {
            tour.setStatus(0); // Sold out
        }
        tourRepository.save(tour);

        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(Integer bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Kiểm tra xem booking có phải của user này không
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // Chỉ có thể hủy booking đang pending
        if (booking.getStatus() != 0) {
            throw new RuntimeException("Cannot cancel this booking");
        }

        // Cập nhật trạng thái booking
        booking.setStatus(2); // Cancelled

        // Hoàn trả số chỗ trống cho tour
        Tour tour = booking.getTour();
        tour.setAvailableSlots(tour.getAvailableSlots() + booking.getNumberOfPeople());
        if (tour.getStatus() == 0 && tour.getAvailableSlots() > 0) {
            tour.setStatus(1); // Available again
        }
        tourRepository.save(tour);

        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookingDateDesc();
    }

    @Transactional
    public void confirmBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != 0) {
            throw new RuntimeException("Can only confirm pending bookings");
        }

        booking.setStatus(1); // Confirmed
        bookingRepository.save(booking);
    }

    @Transactional
    public void rejectBooking(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != 0) {
            throw new RuntimeException("Can only reject pending bookings");
        }

        booking.setStatus(2); // Rejected/Cancelled

        // Hoàn trả số chỗ trống cho tour
        Tour tour = booking.getTour();
        tour.setAvailableSlots(tour.getAvailableSlots() + booking.getNumberOfPeople());
        if (tour.getStatus() == 0 && tour.getAvailableSlots() > 0) {
            tour.setStatus(1); // Available again
        }
        tourRepository.save(tour);

        bookingRepository.save(booking);
    }

    public long getTotalBookings() {
        return bookingRepository.count();
    }

    public long getPendingBookingsCount() {
        return bookingRepository.countByStatus(0);
    }

    public List<Booking> getRecentBookings() {
        return bookingRepository.findTop5ByOrderByBookingDateDesc();
    }

    public List<Booking> getBookingsByTour(Tour tour) {
        return bookingRepository.findByTourOrderByBookingDateDesc(tour);
    }

    public long countBookings() {
        return bookingRepository.count();
    }

    public double calculateTotalRevenue() {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getStatus() == 1) // Chỉ tính các booking đã confirm
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }
} 