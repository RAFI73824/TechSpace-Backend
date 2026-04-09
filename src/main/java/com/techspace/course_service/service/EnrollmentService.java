package com.techspace.course_service.service;

import java.util.List;

import com.techspace.course_service.entity.Enrollment;

public interface EnrollmentService {

    Enrollment enrollCourse(Enrollment enrollment);

    List<Enrollment> getEnrollmentsByUser(Long userId);

}