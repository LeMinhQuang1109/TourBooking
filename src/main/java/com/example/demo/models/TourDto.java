package com.example.demo.models;

import jakarta.validation.constraints.*;

public class TourDto {
    private int id;              // ID của tour, kiểu int, phù hợp với BIGINT trong DB
    
    @NotBlank(message = "Name is required")
    private String name;          // Tên của tour
    
    @NotBlank(message = "Description is required")
    private String description;   // Mô tả của tour
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;         // Giá của tour
    
    private Integer duration;     // Thời gian (kiểu Integer, có thể là NULL)
    private String location;      // Địa điểm

    
 // Constructor không tham số (bắt buộc với Hibernate)
    public TourDto() {
    }


public TourDto(@NotBlank(message = "Name is required") String name,
		@NotBlank(message = "Description is required") String description,
		@NotNull(message = "Price is required") @Positive(message = "Price must be positive") Double price,
		Integer duration, String location) {
	super();
	this.name = name;
	this.description = description;
	this.price = price;
	this.duration = duration;
	this.location = location;
}


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


public Double getPrice() {
	return price;
}


public void setPrice(Double price) {
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
    
    
}
