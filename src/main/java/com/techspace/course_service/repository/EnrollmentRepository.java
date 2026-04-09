package com.techspace.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUserId(Long userId);
    // Method to count all enrollments
    long count();

    // Method to count completed enrollments
    long countByCompletedTrue();
 // Query to count enrollments where the course is completed
    
    
    // Query to count completed courses for a specific user and course
    long countByUserIdAndCourseIdAndCompletedTrue(Long userId, Long courseId);

}