package com.techspace.course_service.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.entity.Certificate;
import com.techspace.course_service.service.CertificateService;
import com.techspace.course_service.service.EmailService;
import com.techspace.course_service.util.CertificatePdfGenerator;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final EmailService emailService;

    /**
     * Generate certificate and send email automatically with the PDF attachment
     */
    @PostMapping("/generate")
    public Certificate generateCertificate(@RequestParam Long userId, @RequestParam Long courseId) {
        // Generate certificate
        Certificate certificate = certificateService.generateCertificate(userId, courseId);

        // Generate PDF for the certificate
        byte[] pdf = CertificatePdfGenerator.generatePdf(certificate);

        // Send email with the PDF attached
        emailService.sendCertificateIssuedEmail(certificate.getUser().getEmail(), certificate.getCertificateCode(), pdf);

        return certificate;
    }

    /**
     * Get all certificates for a user
     */
    @GetMapping("/user/{userId}")
    public List<Certificate> getUserCertificates(@PathVariable Long userId) {
        return certificateService.getUserCertificates(userId);
    }

    /**
     * Verify certificate by certificate code
     */
    @GetMapping("/verify/{code}")
    public Certificate verifyCertificate(@PathVariable String code) {
        return certificateService.getCertificateByCode(code);
    }

    /**
     * Download certificate as PDF
     */
    @GetMapping("/download/{code}")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable String code) {

        Certificate certificate = certificateService.verifyCertificate(code);

        byte[] pdf = CertificatePdfGenerator.generatePdf(certificate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate-" + code + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
}