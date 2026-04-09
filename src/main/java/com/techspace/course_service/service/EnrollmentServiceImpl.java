package com.techspace.course_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.Enrollment;
import com.techspace.course_service.exception.AlreadyEnrolledException;
import com.techspace.course_service.repository.EnrollmentRepository;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment enrollCourse(Enrollment enrollment) {

        Long userId = enrollment.getUser().getId();
        Long courseId = enrollment.getCourse().getId();

        boolean exists = enrollmentRepository
                .findByUserId(userId)
                .stream()
                .anyMatch(e -> e.getCourse().getId().equals(courseId));

        if (exists) {
            throw new AlreadyEnrolledException("User already enrolled in this course");
        }

        enrollment.setEnrolledAt(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(Long userId) {

        return enrollmentRepository.findByUserId(userId);
    }
}