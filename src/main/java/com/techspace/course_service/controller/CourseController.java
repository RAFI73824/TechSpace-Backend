package com.techspace.course_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.api.ApiResponse;
import com.techspace.course_service.dto.CourseRequest;
import com.techspace.course_service.dto.CourseResponse;
import com.techspace.course_service.service.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public CourseResponse createCourse(@Valid @RequestBody CourseRequest request) {
        return courseService.createCourse(request);
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseResponse getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

   

    // ✅ Category filter
    @GetMapping("/category/{category}")
    public List<CourseResponse> getCoursesByCategory(@PathVariable String category) {
        return courseService.getCoursesByCategory(category);
    }

    // ✅ Active courses
    @GetMapping("/active")
    public List<CourseResponse> getActiveCourses() {
        return courseService.getActiveCourses();
    }

    // ✅ Active + Category
    @GetMapping("/active/category/{category}")
    public List<CourseResponse> getActiveCoursesByCategory(@PathVariable String category) {
        return courseService.getActiveCoursesByCategory(category);
    }
    
    
    @GetMapping("/search")
    public List<CourseResponse> searchCourses(@RequestParam String keyword) {
        return courseService.searchCourses(keyword);
    }
    
    @GetMapping("/price-range")
    public ApiResponse<List<CourseResponse>> getCoursesByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {

        return ApiResponse.<List<CourseResponse>>builder()
                .success(true)
                .message("Courses filtered by price range")
                .data(courseService.getCoursesByPriceRange(min, max))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
    @GetMapping("/instructor/{instructorId}")
    public ApiResponse<List<CourseResponse>> getCoursesByInstructor(
            @PathVariable Long instructorId) {

        return ApiResponse.<List<CourseResponse>>builder()
                .success(true)
                .message("Courses fetched by instructor")
                .data(courseService.getCoursesByInstructor(instructorId))
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Course deleted successfully")
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }
}