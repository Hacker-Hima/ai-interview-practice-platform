package com.himachalam.aiinterview.controller;

import com.himachalam.aiinterview.model.User;
import com.himachalam.aiinterview.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String profile(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam(required = false) String college,
                                @RequestParam(required = false) String department,
                                @RequestParam(required = false) Integer graduationYear,
                                Authentication auth,
                                RedirectAttributes flash) {
        User user = userService.findByEmail(auth.getName());
        user.setName(name);
        user.setCollege(college);
        user.setDepartment(department);
        user.setGraduationYear(graduationYear);
        userService.updateProfile(user);
        flash.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes flash) {
        if (!newPassword.equals(confirmPassword)) {
            flash.addFlashAttribute("pwError", "New passwords do not match.");
            return "redirect:/profile";
        }
        if (newPassword.length() < 6) {
            flash.addFlashAttribute("pwError", "Password must be at least 6 characters.");
            return "redirect:/profile";
        }
        try {
            User user = userService.findByEmail(auth.getName());
            userService.changePassword(user, currentPassword, newPassword);
            flash.addFlashAttribute("pwSuccess", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            flash.addFlashAttribute("pwError", e.getMessage());
        }
        return "redirect:/profile";
    }
}
