package com.techspace.course_service.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.techspace.course_service.dto.CourseRequest;
import com.techspace.course_service.dto.CourseResponse;

public interface CourseService {

    CourseResponse createCourse(CourseRequest request);

    List<CourseResponse> getAllCourses();

    CourseResponse getCourseById(Long id);

    CourseResponse updateCourse(Long id, CourseRequest request);

    void deleteCourse(Long id);

    Page<CourseResponse> getCoursesWithPagination(int page, int size);

    // ✅ FIXED
    List<CourseResponse> getCoursesByCategory(String category);

    List<CourseResponse> getActiveCourses();

    List<CourseResponse> getActiveCoursesByCategory(String category);

    List<CourseResponse> searchCourses(String keyword);

    List<CourseResponse> getCoursesByPriceRange(Double min, Double max);

    List<CourseResponse> getCoursesByInstructor(Long instructorId);
}