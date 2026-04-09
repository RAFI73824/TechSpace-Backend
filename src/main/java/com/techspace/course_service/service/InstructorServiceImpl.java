package com.techspace.course_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.techspace.course_service.dto.InstructorRequest;
import com.techspace.course_service.dto.InstructorResponse;
import com.techspace.course_service.entity.Instructor;
import com.techspace.course_service.exception.ResourceNotFoundException;
import com.techspace.course_service.repository.InstructorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Override
    public InstructorResponse createInstructor(InstructorRequest request) {

        Instructor instructor = Instructor.builder()
                .name(request.getName())
                .bio(request.getBio())
                .expertise(request.getExpertise())
                .rating(request.getRating())
                .build();

        Instructor saved = instructorRepository.save(instructor);

        return mapToResponse(saved);
    }

    @Override
    public List<InstructorResponse> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InstructorResponse getInstructorById(Long id) {

        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));

        return mapToResponse(instructor);
    }

    @Override
    public InstructorResponse updateInstructor(Long id, InstructorRequest request) {

        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));

        instructor.setName(request.getName());
        instructor.setBio(request.getBio());
        instructor.setExpertise(request.getExpertise());
        instructor.setRating(request.getRating());

        Instructor updated = instructorRepository.save(instructor);

        return mapToResponse(updated);
    }

    @Override
    public void deleteInstructor(Long id) {

        if (!instructorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Instructor not found with id: " + id);
        }

        instructorRepository.deleteById(id);
    }

    private InstructorResponse mapToResponse(Instructor instructor) {
        return InstructorResponse.builder()
                .id(instructor.getId())
                .name(instructor.getName())
                .bio(instructor.getBio())
                .expertise(instructor.getExpertise())
                .rating(instructor.getRating())
                .build();
    }
}