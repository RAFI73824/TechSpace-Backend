package com.techspace.course_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}