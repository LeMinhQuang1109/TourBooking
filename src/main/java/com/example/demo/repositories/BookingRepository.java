package com.example.demo.repositories;

import com.example.demo.models.Booking;
import com.example.demo.models.Tour;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserOrderByBookingDateDesc(User user);
    List<Booking> findAllByOrderByBookingDateDesc();
    long countByStatus(Integer status);
    List<Booking> findTop5ByOrderByBookingDateDesc();
    List<Booking> findByTourOrderByBookingDateDesc(Tour tour);
    boolean existsByUserIdAndTourIdAndStatus(Integer userId, Integer tourId, Integer status);
} 