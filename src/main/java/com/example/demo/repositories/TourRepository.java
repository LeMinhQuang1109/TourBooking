package com.example.demo.repositories;

import com.example.demo.models.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {
    // Các phương thức tìm kiếm có thể thêm ở đây
    // Ví dụ:
    // List<Tour> findByLocation(String location);
    // List<Tour> findByPriceLessThan(double price);
} 