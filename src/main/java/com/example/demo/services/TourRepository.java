package com.example.demo.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Tour;

public interface TourRepository extends JpaRepository<Tour, Integer> {

}