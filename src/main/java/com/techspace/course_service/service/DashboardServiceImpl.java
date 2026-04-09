package com.techspace.course_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.dto.DashboardsStatsDto;
import com.techspace.course_service.repository.CertificateRepository;
import com.techspace.course_service.repository.CourseRepository;
import com.techspace.course_service.repository.EnrollmentRepository;
import com.techspace.course_service.repository.UserRepository;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public DashboardsStatsDto getDashboardStats() {
        // Calculate total users
        long totalUsers = userRepository.count();
        
        // Calculate total courses
        long totalCourses = courseRepository.count();

        // Calculate total enrollments
        long totalEnrollments = enrollmentRepository.count();

        // Calculate total completed courses (enrollments)
        long totalCompletedCourses = enrollmentRepository.countByCompletedTrue();

        // Calculate total certificates issued
        long totalCertificatesIssued = certificateRepository.count();

        return new DashboardsStatsDto(totalUsers, totalCourses, totalEnrollments, totalCompletedCourses, totalCertificatesIssued);
    }
}