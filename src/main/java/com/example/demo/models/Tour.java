package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private int id;

    @Column(nullable = false, length = 255) // Tên tour, không được null
    private String name;

    @Column(length = 255) // Mô tả tour
    private String description;

    @Column(nullable = false) // Giá tour, không được null
    private double price;

    @Column // Thời gian tour (số ngày)
    private Integer duration;

    @Column(length = 255) // Địa điểm tour
    private String location;

    @Column(name = "created_at", updatable = false) // Ngày tạo (không thay đổi sau khi tạo)
    private LocalDateTime createdAt;

    // Constructor không tham số (bắt buộc với Hibernate)
    public Tour() {
    }

    // Constructor đầy đủ
    public Tour(String name, String description, double price, Integer duration, String location, LocalDateTime createdAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.location = location;
        this.createdAt = createdAt;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", location='" + location + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
