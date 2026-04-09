package com.techspace.course_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techspace.course_service.api.ApiResponse;
import com.techspace.course_service.dto.InstructorRequest;
import com.techspace.course_service.dto.InstructorResponse;
import com.techspace.course_service.service.InstructorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instructors")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    // Create Instructor (WRAPPED)
    @PostMapping
    public ApiResponse<InstructorResponse> createInstructor(
            @Valid @RequestBody InstructorRequest request) {

        InstructorResponse response = instructorService.createInstructor(request);

        return ApiResponse.<InstructorResponse>builder()
                .success(true)
                .message("Instructor created successfully")
                .data(response)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Get All Instructors
    @GetMapping
    public ApiResponse<List<InstructorResponse>> getAllInstructors() {

        return ApiResponse.<List<InstructorResponse>>builder()
                .success(true)
                .message("Instructors fetched successfully")
                .data(instructorService.getAllInstructors())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Get Instructor By ID
    @GetMapping("/{id}")
    public ApiResponse<InstructorResponse> getInstructorById(@PathVariable Long id) {

        return ApiResponse.<InstructorResponse>builder()
                .success(true)
                .message("Instructor fetched successfully")
                .data(instructorService.getInstructorById(id))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Update Instructor
    @PutMapping("/{id}")
    public ApiResponse<InstructorResponse> updateInstructor(
            @PathVariable Long id,
            @Valid @RequestBody InstructorRequest request) {

        return ApiResponse.<InstructorResponse>builder()
                .success(true)
                .message("Instructor updated successfully")
                .data(instructorService.updateInstructor(id, request))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // Delete Instructor
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteInstructor(@PathVariable Long id) {

        instructorService.deleteInstructor(id);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Instructor deleted successfully")
                .data("Deleted")
                .timestamp(LocalDateTime.now())
                .build();
    }
}