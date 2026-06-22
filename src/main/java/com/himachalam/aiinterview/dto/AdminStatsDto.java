package com.himachalam.aiinterview.dto;

/**
 * Admin statistics dashboard summary.
 */
public class AdminStatsDto {

    private long totalUsers;
    private long totalQuestions;
    private long totalDomains;
    private long totalInterviews;
    private double overallAvgScore;

    public AdminStatsDto() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(long totalQuestions) { this.totalQuestions = totalQuestions; }

    public long getTotalDomains() { return totalDomains; }
    public void setTotalDomains(long totalDomains) { this.totalDomains = totalDomains; }

    public long getTotalInterviews() { return totalInterviews; }
    public void setTotalInterviews(long totalInterviews) { this.totalInterviews = totalInterviews; }

    public double getOverallAvgScore() { return overallAvgScore; }
    public void setOverallAvgScore(double overallAvgScore) { this.overallAvgScore = overallAvgScore; }
}
