package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.dto.RegisterDto;
import com.himachalam.aiinterview.model.User;

public interface UserService {

    void registerUser(RegisterDto dto);

    User findByEmail(String email);

    void updateProfile(User user);

    void changePassword(User user, String currentPassword, String newPassword);

    boolean isEmailTaken(String email);

    long countStudents();
}