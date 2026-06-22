package com.himachalam.aiinterview.controller;

import com.himachalam.aiinterview.model.Domain;
import com.himachalam.aiinterview.model.Question;
import com.himachalam.aiinterview.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final DomainService domainService;
    private final QuestionService questionService;

    public AdminController(AdminService adminService,
                           DomainService domainService,
                           QuestionService questionService) {
        this.adminService = adminService;
        this.domainService = domainService;
        this.questionService = questionService;
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("stats", adminService.getStats());
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/dashboard";
    }

    // ── Domain Management ─────────────────────────────────────────────────────

    @GetMapping("/domains")
    public String domains(Model model) {
        model.addAttribute("domains", domainService.getAllDomains());
        model.addAttribute("domain", new Domain());
        return "admin/domains";
    }

    @PostMapping("/domains/save")
    public String saveDomain(@ModelAttribute Domain domain, RedirectAttributes flash) {
        domainService.save(domain);
        flash.addFlashAttribute("success",
                (domain.getId() == null ? "Domain added" : "Domain updated") + " successfully.");
        return "redirect:/admin/domains";
    }

    @GetMapping("/domains/edit/{id}")
    public String editDomain(@PathVariable Long id, Model model) {
        model.addAttribute("domains", domainService.getAllDomains());
        model.addAttribute("domain", domainService.getById(id));
        return "admin/domains";
    }

    @GetMapping("/domains/delete/{id}")
    public String deleteDomain(@PathVariable Long id, RedirectAttributes flash) {
        domainService.delete(id);
        flash.addFlashAttribute("success", "Domain deleted.");
        return "redirect:/admin/domains";
    }

    // ── Question Management ───────────────────────────────────────────────────

    @GetMapping("/questions")
    public String questions(@RequestParam(required = false) Long domainId,
                            @RequestParam(required = false) String difficulty,
                            @RequestParam(required = false) String keyword,
                            Model model) {
        List<Question> questions;
        if (keyword != null && !keyword.isBlank()) {
            questions = questionService.search(keyword);
        } else {
            questions = questionService.getByDomainAndDifficulty(domainId, difficulty);
        }
        model.addAttribute("questions", questions);
        model.addAttribute("domains", domainService.getAllDomains());
        model.addAttribute("question", new Question());
        model.addAttribute("selectedDomainId", domainId);
        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("keyword", keyword);
        return "admin/questions";
    }

    @GetMapping("/questions/add")
    public String addQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("domains", domainService.getAllDomains());
        return "admin/question-form";
    }

    @PostMapping("/questions/save")
    public String saveQuestion(@ModelAttribute Question question,
                               @RequestParam Long domainId,
                               RedirectAttributes flash) {
        Domain domain = domainService.getById(domainId);
        question.setDomain(domain);
        questionService.save(question);
        flash.addFlashAttribute("success",
                (question.getId() == null ? "Question added" : "Question updated") + " successfully.");
        return "redirect:/admin/questions";
    }

    @GetMapping("/questions/edit/{id}")
    public String editQuestion(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionService.getById(id));
        model.addAttribute("domains", domainService.getAllDomains());
        return "admin/question-form";
    }

    @GetMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable Long id, RedirectAttributes flash) {
        questionService.delete(id);
        flash.addFlashAttribute("success", "Question deleted.");
        return "redirect:/admin/questions";
    }

    // ── User Management ───────────────────────────────────────────────────────

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes flash) {
        adminService.deleteUser(id);
        flash.addFlashAttribute("success", "User deleted.");
        return "redirect:/admin/users";
    }
}
