package com.techspace.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // 🔥 FIXED (case-insensitive)
    List<Course> findByCategoryIgnoreCase(String category);

    // 🔥 FIXED (case-insensitive + active)
    List<Course> findByCategoryIgnoreCaseAndActiveTrue(String category);

    // Existing
    List<Course> findByActiveTrue();

    List<Course> findByTitleContainingIgnoreCase(String keyword);

    List<Course> findByPriceBetween(Double min, Double max);

    List<Course> findByInstructorId(Long instructorId);
}