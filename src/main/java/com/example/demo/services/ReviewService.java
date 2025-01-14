package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Review;
import com.example.demo.repositories.BookingRepository;
import com.example.demo.repositories.ReviewRepository;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BookingRepository bookingRepository;

    public boolean canUserReviewTour(Integer userId, Integer tourId) {
        // Kiểm tra xem user đã đặt tour này và tour đã được xác nhận
        return bookingRepository.existsByUserIdAndTourIdAndStatus(userId, tourId, 1);
    }

    public Review addReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<Review> getTourReviews(Integer tourId) {
        return reviewRepository.findByTourIdOrderByCreatedAtDesc(tourId);
    }

    public boolean hasUserReviewedTour(Integer userId, Integer tourId) {
        return reviewRepository.existsByUserIdAndTourId(userId, tourId);
    }

    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(Integer id) {
        reviewRepository.deleteById(id);
    }

    public Review getReviewById(Integer id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
} 