package com.techspace.course_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByCertificateCode(String certificateCode);

    List<Certificate> findByUserId(Long userId);

    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    Certificate findByUserIdAndCourseId(Long userId, Long courseId);
}