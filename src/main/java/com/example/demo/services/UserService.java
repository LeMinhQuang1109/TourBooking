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
            .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Transactional
    public void changeUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(newRole);
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public void updateUser(Long userId, User updatedUser) {
        User user = findById(userId);
        
        // Kiểm tra username mới có bị trùng không (nếu username thay đổi)
        if (!user.getUsername().equals(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
        }

        // Kiểm tra email mới có bị trùng không (nếu email thay đổi)
        if (!user.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        // Cập nhật thông tin
        user.setUsername(updatedUser.getUsername());
        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());

        // Cập nhật mật khẩu nếu có
        String newPassword = updatedUser.getPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findById(userId);
        
        // Không cho phép xóa tài khoản admin cuối cùng
        if (user.getRole().equals("ROLE_ADMIN")) {
            long adminCount = userRepository.countByRole("ROLE_ADMIN");
            if (adminCount <= 1) {
                throw new RuntimeException("Cannot delete the last admin account");
            }
        }
        
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            true, // Luôn active
            true, // accountNonExpired
            true, // credentialsNonExpired
            true, // accountNonLocked
            getAuthorities(user.getRole())
        );
    }
}