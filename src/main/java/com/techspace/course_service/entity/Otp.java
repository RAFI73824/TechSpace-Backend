package com.techspace.course_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Otp {

    @Id
    private Long id;
    private String phoneNumber;
    private String otp;

    // Getters and setters
}