package com.techspace.course_service.dto;

public class DashboardsStatsDto {

    private long totalUsers;
    private long totalCourses;
    private long totalEnrollments;
    private long totalCompletedCourses;
    private long totalCertificatesIssued;

    // Constructor
    public DashboardsStatsDto(long totalUsers, long totalCourses, long totalEnrollments,
                              long totalCompletedCourses, long totalCertificatesIssued) {
        this.totalUsers = totalUsers;
        this.totalCourses = totalCourses;
        this.totalEnrollments = totalEnrollments;
        this.totalCompletedCourses = totalCompletedCourses;
        this.totalCertificatesIssued = totalCertificatesIssued;
    }

    // Getters and setters
    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public long getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(long totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public long getTotalCompletedCourses() {
        return totalCompletedCourses;
    }

    public void setTotalCompletedCourses(long totalCompletedCourses) {
        this.totalCompletedCourses = totalCompletedCourses;
    }

    public long getTotalCertificatesIssued() {
        return totalCertificatesIssued;
    }

    public void setTotalCertificatesIssued(long totalCertificatesIssued) {
        this.totalCertificatesIssued = totalCertificatesIssued;
    }
}