package com.techspace.course_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Send certificate issued email (TEXT)
    public void sendCertificateIssuedEmail(String toEmail, String certificateCode) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🎓 Your TechSpace Certificate");
        message.setText(
                "Congratulations! Your certificate has been issued.\n\nCertificate Code: "
                        + certificateCode);

        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new RuntimeException("Error sending certificate issued email", ex);
        }
    }

    // Send course enrollment email
    public void sendCourseEnrolledEmail(String toEmail, String courseName) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Course Enrollment Confirmation");
        message.setText("You have successfully enrolled in the course: " + courseName);

        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new RuntimeException("Error sending course enrollment email", ex);
        }
    }

    // Send password reset email
    public void sendPasswordResetEmail(String toEmail, String resetLink) {

        String html = """
            <h2>🔐 Password Reset</h2>
            <p>Click the button below to reset your password:</p>
            <a href="%s" style="padding:10px 20px;background:#6366f1;color:white;text-decoration:none;border-radius:5px;">
                Reset Password
            </a>
            <p>This link is valid for 15 minutes.</p>
            """.formatted(resetLink);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password - TechSpace");
            helper.setText(html, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error sending reset email", e);
        }
    }

    // Send certificate email with PDF attachment
    public void sendCertificateIssuedEmail(String toEmail, String certificateCode, byte[] pdfBytes) {

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

        sendHtmlEmailWithAttachment(toEmail, "Your TechSpace Certificate", html, certificateCode, pdfBytes);
    }

    // HTML Email Sender with Attachment
    private void sendHtmlEmailWithAttachment(String toEmail, String subject, String htmlBody,
            String certificateCode, byte[] pdfBytes) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            helper.addAttachment(
                    "certificate-" + certificateCode + ".pdf",
                    new org.springframework.core.io.ByteArrayResource(pdfBytes));

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email with certificate", e);
        }
    }
}