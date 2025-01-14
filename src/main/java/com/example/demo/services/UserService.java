package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.User;
import com.example.demo.models.UserDto;
import com.example.demo.models.UserProfileDto;
import com.example.demo.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public List<User> getRecentUsers() {
        return userRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateProfile(String username, UserProfileDto profileDto) {
        User user = findByUsername(username);
        
        // Kiểm tra email mới có bị trùng không (nếu email thay đổi)
        if (!user.getEmail().equals(profileDto.getEmail())) {
            if (userRepository.existsByEmail(profileDto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        // Cập nhật thông tin cơ bản
        user.setFullName(profileDto.getFullName());
        user.setEmail(profileDto.getEmail());

        // Cập nhật mật khẩu nếu được cung cấp
        if (profileDto.getNewPassword() != null && !profileDto.getNewPassword().isEmpty()) {
            // Kiểm tra mật khẩu hiện tại
            if (profileDto.getCurrentPassword() == null || 
                !passwordEncoder.matches(profileDto.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            
            // Kiểm tra mật khẩu mới và xác nhận mật khẩu
            if (!profileDto.getNewPassword().equals(profileDto.getConfirmPassword())) {
                throw new RuntimeException("New passwords don't match");
            }
            
            // Cập nhật mật khẩu mới
            user.setPassword(passwordEncoder.encode(profileDto.getNewPassword()));
        }

        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void changeUserRole(Integer userId, String newRole) {
        User user = findById(userId);
        user.setRole(newRole);
        userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void updateUser(Integer userId, User updatedUser) {
        User user = findById(userId);
        
        // Kiểm tra username mới có bị trùng không
        if (!user.getUsername().equals(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
        }

        // Kiểm tra email mới có bị trùng không
        if (!user.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setFullName(updatedUser.getFullName());
        user.setRole(updatedUser.getRole());

        // Cập nhật password nếu có
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public long countUsers() {
        return userRepository.count();
    }
}