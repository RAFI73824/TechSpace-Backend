package com.techspace.course_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.techspace.course_service.dto.CourseRequest;
import com.techspace.course_service.dto.CourseResponse;
import com.techspace.course_service.entity.Course;
import com.techspace.course_service.entity.Instructor;
import com.techspace.course_service.exception.ResourceNotFoundException;
import com.techspace.course_service.repository.CourseRepository;
import com.techspace.course_service.repository.InstructorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    // ✅ CREATE
    @Override
    public CourseResponse createCourse(CourseRequest request) {

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + request.getInstructorId()));

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .active(request.getActive())
                .instructor(instructor)
                .build();

        return mapToResponse(courseRepository.save(course));
    }

    // ✅ GET ALL
    @Override
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET BY ID
    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        return mapToResponse(course);
    }

    // ✅ UPDATE
    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + request.getInstructorId()));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCategory(request.getCategory());
        course.setPrice(request.getPrice());
        course.setActive(request.getActive());
        course.setInstructor(instructor);

        return mapToResponse(courseRepository.save(course));
    }

    // ✅ DELETE
    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    // ✅ PAGINATION
    @Override
    public Page<CourseResponse> getCoursesWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return courseRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // 🔥 FIXED CATEGORY (case-insensitive)
    @Override
    public List<CourseResponse> getCoursesByCategory(String category) {
        return courseRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ ACTIVE
    @Override
    public List<CourseResponse> getActiveCourses() {
        return courseRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔥 FIXED ACTIVE + CATEGORY
    @Override
    public List<CourseResponse> getActiveCoursesByCategory(String category) {
        return courseRepository.findByCategoryIgnoreCaseAndActiveTrue(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ SEARCH
    @Override
    public List<CourseResponse> searchCourses(String keyword) {
        return courseRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ PRICE RANGE
    @Override
    public List<CourseResponse> getCoursesByPriceRange(Double min, Double max) {
        return courseRepository.findByPriceBetween(min, max)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ✅ INSTRUCTOR
    @Override
    public List<CourseResponse> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔥 MAPPER
    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .price(course.getPrice())
                .active(course.getActive())
                .build();
    }
}