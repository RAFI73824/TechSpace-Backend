package com.techspace.course_service.service;

import java.util.List;
import com.techspace.course_service.dto.InstructorRequest;
import com.techspace.course_service.dto.InstructorResponse;

public interface InstructorService {

    InstructorResponse createInstructor(InstructorRequest request);

    List<InstructorResponse> getAllInstructors();

    InstructorResponse getInstructorById(Long id);

    InstructorResponse updateInstructor(Long id, InstructorRequest request);

    void deleteInstructor(Long id);
}