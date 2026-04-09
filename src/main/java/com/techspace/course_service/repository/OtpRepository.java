package com.techspace.course_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByPhoneNumber(String phoneNumber);
}