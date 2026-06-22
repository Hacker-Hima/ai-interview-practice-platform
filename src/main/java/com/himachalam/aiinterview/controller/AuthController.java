package com.himachalam.aiinterview.controller;

import com.himachalam.aiinterview.dto.RegisterDto;
import com.himachalam.aiinterview.exception.EmailAlreadyExistsException;
import com.himachalam.aiinterview.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterDto registerDto, Model model) {
        // Basic validation
        if (registerDto.getName() == null || registerDto.getName().trim().isBlank()) {
            model.addAttribute("error", "Name is required.");
            model.addAttribute("registerDto", registerDto);
            return "register";
        }
        if (registerDto.getPassword() == null || registerDto.getPassword().length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            model.addAttribute("registerDto", registerDto);
            return "register";
        }
        try {
            userService.registerUser(registerDto);
            return "redirect:/login?registered";
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("error", "This email is already registered. Please log in.");
            model.addAttribute("registerDto", registerDto);
            return "register";
        }
    }
}