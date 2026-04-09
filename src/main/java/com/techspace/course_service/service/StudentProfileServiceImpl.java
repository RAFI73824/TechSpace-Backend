package com.techspace.course_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.Certificate;
import com.techspace.course_service.entity.ContentProgress;
import com.techspace.course_service.entity.Course;
import com.techspace.course_service.entity.Enrollment;
import com.techspace.course_service.entity.User;
import com.techspace.course_service.repository.CertificateRepository;
import com.techspace.course_service.repository.ContentProgressRepository;
import com.techspace.course_service.repository.EnrollmentRepository;
import com.techspace.course_service.repository.UserRepository;

@Service
public class StudentProfileServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ContentProgressRepository contentProgressRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    // Get user profile by userId
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Get enrolled courses for the user
    public List<Course> getEnrolledCourses(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    // Get completed courses for the user
    public List<Course> getCompletedCourses(Long userId) {
        List<ContentProgress> contentProgressList = contentProgressRepository.findByUserId(userId); // This is the updated call
        return contentProgressList.stream()
        		.filter(ContentProgress::isCompleted) // Filter completed content progress
                .map(contentProgress -> contentProgress.getContent().getSection().getCourse())
                .collect(Collectors.toList());
    }

    // Get certificates earned by the user
    public List<Certificate> getCertificates(Long userId) {
        return certificateRepository.findByUserId(userId);
    }

    // Update user profile information (name, email, phone)
    public User updateUserProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }
}