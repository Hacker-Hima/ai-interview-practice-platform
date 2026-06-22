package com.himachalam.aiinterview.service;

import com.himachalam.aiinterview.dto.AdminStatsDto;
import com.himachalam.aiinterview.model.User;
import java.util.List;

public interface AdminService {
    AdminStatsDto getStats();
    List<User> getAllUsers();
    void deleteUser(Long id);
}
