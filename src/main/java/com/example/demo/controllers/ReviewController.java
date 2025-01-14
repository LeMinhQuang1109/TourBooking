package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.models.Review;
import com.example.demo.models.User;
import com.example.demo.services.ReviewService;
import com.example.demo.services.UserService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addReview(@RequestBody Review review, Principal principal) {
        // Lấy user từ username
        User user = userService.findByUsername(principal.getName());
        
        if (!reviewService.canUserReviewTour(user.getId(), review.getTour().getId())) {
            return ResponseEntity.badRequest()
                .body("Bạn chỉ có thể đánh giá tour sau khi hoàn thành chuyến đi");
        }

        if (reviewService.hasUserReviewedTour(user.getId(), review.getTour().getId())) {
            return ResponseEntity.badRequest()
                .body("Bạn đã đánh giá tour này rồi");
        }

        review.setUser(user);
        Review savedReview = reviewService.addReview(review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/tour/{tourId}")
    @ResponseBody
    public ResponseEntity<List<Review>> getTourReviews(@PathVariable Integer tourId) {
        return ResponseEntity.ok(reviewService.getTourReviews(tourId));
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateReview(@PathVariable Integer id, 
                                        @RequestBody Review updatedReview, 
                                        Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Review existingReview = reviewService.getReviewById(id);
        
        // Kiểm tra xem review có tồn tại không
        if (existingReview == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Kiểm tra xem người dùng có phải là người tạo review không
        if (!existingReview.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403)
                .body("Bạn không có quyền sửa đánh giá này");
        }
        
        // Cập nhật nội dung review
        existingReview.setRating(updatedReview.getRating());
        existingReview.setComment(updatedReview.getComment());
        
        Review savedReview = reviewService.updateReview(existingReview);
        return ResponseEntity.ok(savedReview);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteReview(@PathVariable Integer id, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Review review = reviewService.getReviewById(id);
        
        // Kiểm tra xem review có tồn tại không
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Kiểm tra xem người dùng có phải là người tạo review không
        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403)
                .body("Bạn không có quyền xóa đánh giá này");
        }
        
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
} 