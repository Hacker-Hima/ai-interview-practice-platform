package com.himachalam.aiinterview.controller;

import com.himachalam.aiinterview.model.Answer;
import com.himachalam.aiinterview.model.InterviewSession;
import com.himachalam.aiinterview.model.User;
import com.himachalam.aiinterview.service.InterviewService;
import com.himachalam.aiinterview.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private final UserService userService;
    private final InterviewService interviewService;

    public HistoryController(UserService userService, InterviewService interviewService) {
        this.userService = userService;
        this.interviewService = interviewService;
    }

    @GetMapping
    public String history(Authentication auth, Model model) {
        User user = userService.findByEmail(auth.getName());
        List<InterviewSession> sessions = interviewService.getSessionsByUser(user);
        model.addAttribute("sessions", sessions);
        return "history/list";
    }

    @GetMapping("/{sessionId}")
    public String detail(@PathVariable Long sessionId, Model model) {
        InterviewSession session = interviewService.getSessionById(sessionId);
        List<Answer> answers = interviewService.getAnswersForSession(sessionId);
        model.addAttribute("session", session);
        model.addAttribute("answers", answers);
        return "history/detail";
    }
}
