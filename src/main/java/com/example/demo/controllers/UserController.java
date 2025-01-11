package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Admin: Lấy danh sách tất cả users
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }
        
        List<User> users = userRepository.findAll();
        // Ẩn mật khẩu trước khi trả về
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // User & Admin: Xem thông tin profile
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        // Kiểm tra quyền: chỉ admin hoặc chính user đó mới xem được
        if (!"ADMIN".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        
        return userRepository.findById(id)
                .map(user -> {
                    user.setPassword(null); // Ẩn mật khẩu
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // User & Admin: Cập nhật thông tin profile
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        // Kiểm tra quyền
        if (!"ADMIN".equals(currentUser.getRole()) && !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }
        
        return userRepository.findById(id)
                .map(user -> {
                    // Cập nhật thông tin cơ bản
                    if (updates.containsKey("fullName")) {
                        user.setFullName(updates.get("fullName"));
                    }
                    if (updates.containsKey("email")) {
                        // Kiểm tra email mới không trùng với user khác
                        if (!user.getEmail().equals(updates.get("email")) && 
                            userRepository.existsByEmail(updates.get("email"))) {
                            return ResponseEntity.badRequest().body("Email already exists");
                        }
                        user.setEmail(updates.get("email"));
                    }
                    
                    // Cập nhật mật khẩu nếu có
                    if (updates.containsKey("password")) {
                        user.setPassword(passwordEncoder.encode(updates.get("password")));
                    }
                    
                    // Chỉ admin mới có thể cập nhật role
                    if (updates.containsKey("role") && "ADMIN".equals(currentUser.getRole())) {
                        user.setRole(updates.get("role"));
                    }
                    
                    User updatedUser = userRepository.save(user);
                    updatedUser.setPassword(null); // Ẩn mật khẩu trong response
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin: Xóa user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        
        if (!"ADMIN".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied");
        }
        
        // Không cho phép admin tự xóa chính mình
        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Cannot delete your own account");
        }
        
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "User deleted successfully");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // User & Admin: Lấy thông tin user hiện tại
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        currentUser.setPassword(null); // Ẩn mật khẩu
        return ResponseEntity.ok(currentUser);
    }
} 