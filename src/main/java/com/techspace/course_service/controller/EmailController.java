package com.techspace.course_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint to send certificate issued email
    @PostMapping("/send/certificate")
    public void sendCertificateIssuedEmail(@RequestParam String toEmail, @RequestParam String certificateCode) {
        emailService.sendCertificateIssuedEmail(toEmail, certificateCode);
    }

    // Endpoint to send course enrollment email
    @PostMapping("/send/course")
    public void sendCourseEnrolledEmail(@RequestParam String toEmail, @RequestParam String courseName) {
        emailService.sendCourseEnrolledEmail(toEmail, courseName);
    }

    // Endpoint to send password reset email
    @PostMapping("/send/password-reset")
    public void sendPasswordResetEmail(@RequestParam String toEmail, @RequestParam String resetLink) {
        emailService.sendPasswordResetEmail(toEmail, resetLink);
    }
}