package com.techspace.course_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.User;
import com.techspace.course_service.exception.InvalidOtpException;
import com.techspace.course_service.repository.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class OtpService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String phoneNumber;

    private static final String OTP = "123456";  // Example static OTP, replace with actual OTP generation logic

    @Autowired
    private UserRepository userRepository;

    // @PostConstruct ensures Twilio is initialized only after the properties are injected
    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);  // Initialize Twilio with accountSid and authToken
    }

    // Generate OTP (this is a simple example)
    public String generateOtp() {
        // You should generate a random 6-digit OTP instead of using a fixed value
        return OTP;  // Example static OTP, replace with actual OTP generation logic
    }

    // Send OTP using Twilio
    public void sendOtp(String phoneNumber, String otp) {
        try {
            Message.creator(
                    new PhoneNumber(phoneNumber), // To phone number
                    new PhoneNumber(this.phoneNumber), // From your Twilio phone number
                    "Your OTP code is: " + otp  // Message content
            ).create();
        } catch (Exception e) {
            throw new InvalidOtpException("Error sending OTP: " + e.getMessage());
        }
    }

    // Verify OTP (just checking against the static value for now)
    public boolean verifyOtp(String phoneNumber, String otp) {
        // Here you should implement your logic to check OTP validity, e.g., check against a database or cache
        return OTP.equals(otp);  // In production, replace this with real verification logic
    }

    // Check if the phone is verified (based on OTP)
    public boolean isPhoneVerified(String phoneNumber) {
        User user = userRepository.findByPhone(phoneNumber).orElseThrow(() ->
                new InvalidOtpException("Phone number not found"));
        return user.getPhoneVerified();  // Return whether the phone is verified
    }
}