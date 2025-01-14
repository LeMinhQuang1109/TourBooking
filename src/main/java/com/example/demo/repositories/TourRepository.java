package com.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.models.Tour;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    List<Tour> findTop5ByOrderByCreatedAtDesc();
    
    // Thêm các phương thức tìm kiếm mới
    List<Tour> findByStatus(Integer status);
    List<Tour> findByAvailableSlotsGreaterThan(Integer slots);
}