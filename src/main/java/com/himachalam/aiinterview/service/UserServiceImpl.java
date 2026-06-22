package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.dto.RegisterDto;
import com.himachalam.aiinterview.exception.EmailAlreadyExistsException;
import com.himachalam.aiinterview.model.User;
import com.himachalam.aiinterview.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered: " + dto.getEmail());
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("STUDENT");
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    @Override
    public void updateProfile(User user) {
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public long countStudents() {
        return userRepository.findAll().stream()
                .filter(u -> "STUDENT".equals(u.getRole()))
                .count();
    }
}