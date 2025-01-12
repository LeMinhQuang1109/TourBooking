package com.example.demo.config;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cập nhật lại role cho tất cả user hiện có
        List<User> users = userRepository.findAll();
        for (User user : users) {
            // Kiểm tra và sửa role
            if (user.getRole() != null && !user.getRole().startsWith("ROLE_")) {
                String newRole = "ROLE_" + user.getRole();
                user.setRole(newRole);
                userRepository.save(user);
                System.out.println("Updated role for user: " + user.getUsername() + " to: " + newRole);
            }

            // Kiểm tra và mã hóa mật khẩu nếu cần
            if (!user.getPassword().startsWith("$2a$")) {
                String rawPassword = user.getPassword();
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
                System.out.println("Encrypted password for user: " + user.getUsername());
            }
        }

        // Tạo tài khoản admin nếu chưa có
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setFullName("System Administrator");
            admin.setRole("ROLE_ADMIN");
            admin.setActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            
            userRepository.save(admin);
            System.out.println("Admin account created successfully!");
        }
    }
} 