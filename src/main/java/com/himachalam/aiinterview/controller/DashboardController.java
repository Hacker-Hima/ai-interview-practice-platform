package com.himachalam.aiinterview.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.himachalam.aiinterview.model.InterviewSession;
import com.himachalam.aiinterview.model.User;
import com.himachalam.aiinterview.service.DomainService;
import com.himachalam.aiinterview.service.InterviewService;
import com.himachalam.aiinterview.service.UserService;
import com.himachalam.aiinterview.repository.InterviewSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final UserService userService;
    private final InterviewService interviewService;
    private final InterviewSessionRepository sessionRepository;
    private final DomainService domainService;
    private final ObjectMapper objectMapper;

    public DashboardController(UserService userService,
                               InterviewService interviewService,
                               InterviewSessionRepository sessionRepository,
                               DomainService domainService,
                               ObjectMapper objectMapper) {
        this.userService = userService;
        this.interviewService = interviewService;
        this.sessionRepository = sessionRepository;
        this.domainService = domainService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) throws Exception {
        User user = userService.findByEmail(authentication.getName());

        List<InterviewSession> allSessions = interviewService.getSessionsByUser(user);
        List<InterviewSession> completed = allSessions.stream()
                .filter(s -> "COMPLETED".equals(s.getStatus()))
                .sorted(Comparator.comparing(InterviewSession::getStartTime))
                .collect(Collectors.toList());

        // ── Stats ──────────────────────────────────────────────────────────────
        Double avgScore = sessionRepository.findAvgScoreByUser(user);
        Double bestScore = sessionRepository.findMaxScoreByUser(user);

        // ── Chart data (score trend) ───────────────────────────────────────────
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM");
        List<String> chartLabels = completed.stream()
                .map(s -> s.getStartTime().format(fmt))
                .collect(Collectors.toList());
        List<Double> chartScores = completed.stream()
                .map(s -> s.getScore() != null ? s.getScore() : 0.0)
                .collect(Collectors.toList());

        // ── Domain distribution for bar chart ─────────────────────────────────
        Map<String, Long> domainCounts = completed.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getDomain().getName(), Collectors.counting()));

        // Recent 5 sessions
        List<InterviewSession> recent = allSessions.stream().limit(5).collect(Collectors.toList());

        // Last attempt
        Optional<InterviewSession> last = sessionRepository
                .findTopByUserAndStatusOrderByStartTimeDesc(user, "COMPLETED");

        model.addAttribute("user", user);
        model.addAttribute("totalInterviews", completed.size());
        model.addAttribute("avgScore", avgScore != null ? Math.round(avgScore) : 0);
        model.addAttribute("bestScore", bestScore != null ? Math.round(bestScore) : 0);
        model.addAttribute("recentSessions", recent);
        model.addAttribute("lastSession", last.orElse(null));
        model.addAttribute("domains", domainService.getAllDomains());
        model.addAttribute("chartLabels", objectMapper.writeValueAsString(chartLabels));
        model.addAttribute("chartScores", objectMapper.writeValueAsString(chartScores));
        model.addAttribute("domainLabels", objectMapper.writeValueAsString(new ArrayList<>(domainCounts.keySet())));
        model.addAttribute("domainData", objectMapper.writeValueAsString(new ArrayList<>(domainCounts.values())));

        return "dashboard";
    }
}