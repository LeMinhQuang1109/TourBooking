package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTourIdOrderByCreatedAtDesc(Integer tourId);
    boolean existsByUserIdAndTourId(Long userId, Integer tourId);
    Optional<Review> findByUserIdAndTourId(Long userId, Integer tourId);
} 