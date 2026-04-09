package com.techspace.course_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.Certificate;
import com.techspace.course_service.entity.Course;
import com.techspace.course_service.entity.User;
import com.techspace.course_service.repository.CertificateRepository;
import com.techspace.course_service.repository.CourseRepository;
import com.techspace.course_service.repository.UserRepository;
import com.techspace.course_service.util.CertificatePdfGenerator;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ContentProgressService progressService;

    @Autowired
    private JavaMailSender javaMailSender;  // Add JavaMailSender here

    @Override
    public Certificate generateCertificate(Long userId, Long courseId) {

        // Check if course completed
        if (!progressService.isCourseCompleted(userId, courseId)) {
            throw new RuntimeException("Course not completed yet");
        }

        // Prevent duplicate certificate
        Certificate existingCertificate =
                certificateRepository.findByUserIdAndCourseId(userId, courseId);

        if (existingCertificate != null) {
            return existingCertificate;
        }

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get course
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Generate certificate code
        String code = "TECHSPACE-" + UUID.randomUUID().toString().substring(0, 8);

        // Create certificate
        Certificate certificate = Certificate.builder()
                .certificateCode(code)
                .user(user)
                .course(course)
                .issuedAt(LocalDateTime.now())
                .certificateUrl("/certificates/" + code + ".pdf")
                .build();

        // Save certificate first
        certificateRepository.save(certificate);

        // Generate PDF
        byte[] pdf = CertificatePdfGenerator.generatePdf(certificate);

        // Send email with PDF
        sendCertificateIssuedEmail(user.getEmail(), code, pdf);

        return certificate;
    }
    @Override
    public List<Certificate> getUserCertificates(Long userId) {
        return certificateRepository.findByUserId(userId);
    }

    @Override
    public Certificate verifyCertificate(String code) {
        return certificateRepository
                .findByCertificateCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Certificate not found"));
    }

    @Override
    public Certificate getCertificateByCode(String code) {
        return certificateRepository
                .findByCertificateCode(code)
                .orElseThrow(() ->
                        new RuntimeException("Certificate not found"));
    }

    public void sendCertificateIssuedEmail(String toEmail, String certificateCode, byte[] pdfBytes) {
        try {
            System.out.println("Sending certificate email to: " + toEmail);  // Log to verify email triggering

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(toEmail);
            helper.setSubject("🎓 Your TechSpace Certificate");

            String html = """
                <h2>🎉 Certificate Issued</h2>
                <p>Congratulations!</p>
                <p>Your certificate has been issued.</p>
                <p><b>Certificate Code:</b> %s</p>
                <br>
                <p>The certificate PDF is attached.</p>
                <br>
                <p>Regards,<br>TechSpace Academy</p>
            """.formatted(certificateCode);

            helper.setText(html, true);

            // Attach the certificate PDF
            helper.addAttachment("certificate-" + certificateCode + ".pdf", new org.springframework.core.io.ByteArrayResource(pdfBytes));

            javaMailSender.send(message); // Send email
            
            System.out.println("Certificate email sent successfully to: " + toEmail);  // Log success
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());  // Log error if sending fails
            throw new RuntimeException("Error sending certificate email", e);
        }
    }
}