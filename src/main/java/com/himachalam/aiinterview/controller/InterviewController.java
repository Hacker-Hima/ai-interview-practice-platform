package com.himachalam.aiinterview.controller;

import com.himachalam.aiinterview.dto.InterviewSubmitDto;
import com.himachalam.aiinterview.model.Answer;
import com.himachalam.aiinterview.model.InterviewSession;
import com.himachalam.aiinterview.service.DomainService;
import com.himachalam.aiinterview.service.InterviewService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InterviewController {

    private final InterviewService interviewService;
    private final DomainService domainService;

    public InterviewController(InterviewService interviewService,
                               DomainService domainService) {
        this.interviewService = interviewService;
        this.domainService = domainService;
    }

    /** Step 1 – Domain selection page */
    @GetMapping("/interview")
    public String selectDomain(Model model) {
        model.addAttribute("domains", domainService.getAllDomains());
        return "interview/select-domain";
    }

    /** Step 2 – Create session and display 10 random questions */
    @GetMapping("/interview/start")
    public String startInterview(@RequestParam Long domainId,
                                 Authentication auth,
                                 Model model,
                                 RedirectAttributes flash) {
        try {
            InterviewSession session = interviewService.startInterview(auth.getName(), domainId);
            model.addAttribute("interviewSession", session);
            model.addAttribute("questions", session.getQuestions());
            model.addAttribute("domain", session.getDomain());
            return "interview/questions";
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", ex.getMessage());
            return "redirect:/interview";
        }
    }

    /** Step 3 – Submit answers, evaluate, redirect to result */
    @PostMapping("/interview/submit")
    public String submitInterview(@ModelAttribute InterviewSubmitDto submitDto,
                                  RedirectAttributes flash) {
        try {
            InterviewSession session = interviewService.submitInterview(
                    submitDto.getSessionId(), submitDto.getAnswers());
            return "redirect:/result/" + session.getId();
        } catch (RuntimeException ex) {
            flash.addFlashAttribute("error", "Submission failed: " + ex.getMessage());
            return "redirect:/dashboard";
        }
    }

    /** Step 4 – Result page */
    @GetMapping("/result/{sessionId}")
    public String result(@PathVariable Long sessionId, Model model) {
        InterviewSession session = interviewService.getSessionById(sessionId);
        List<Answer> answers = interviewService.getAnswersForSession(sessionId);

        long aiCount = answers.stream().filter(a -> Boolean.TRUE.equals(a.getEvaluatedByAI())).count();

        model.addAttribute("interviewSession", session);
        model.addAttribute("answers", answers);
        model.addAttribute("score", session.getScore() != null ? Math.round(session.getScore()) : 0);
        model.addAttribute("aiEvaluated", aiCount > 0);
        model.addAttribute("aiEvalCount", aiCount);

        return "interview/result";
    }
}