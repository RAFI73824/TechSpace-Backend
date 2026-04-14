package com.techspace.course_service.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    // Use ConcurrentHashMap to store OTPs safely in memory
    private final Map<String, String> otpCache = new ConcurrentHashMap<>();

  @PostConstruct
public void initTwilio() {
    try {
        if (accountSid == null || authToken == null) {
            System.out.println("⚠️ Twilio credentials missing. Skipping initialization.");
            return;
        }

        Twilio.init(accountSid, authToken);
        System.out.println("✅ Twilio initialized successfully");

    } catch (Exception e) {
        System.out.println("❌ Twilio initialization failed: " + e.getMessage());
    }
}

    /**
     * Generates an OTP, stores it in memory, and sends it via SMS.
     */
    public void sendOtp(String phone) {
        // 1. Generate a 6-digit random OTP
        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));

        // 2. Save it to cache (Key: Phone Number, Value: OTP)
        otpCache.put(phone, otp);

        // 3. PRINT TO CONSOLE - This is your lifeline for testing!
        System.out.println("----------------------------------------------");
        System.out.println("DEBUG: OTP generated for " + phone + " is: " + otp);
        System.out.println("----------------------------------------------");

        // 4. Send the real SMS via Twilio
        try {
            Message message = Message.creator(
                    new PhoneNumber(phone),        // To: User's mobile
                    new PhoneNumber(twilioPhoneNumber), // From: Your Twilio number
                    "Your TechSpace verification code is: " + otp
            ).create();
            
            System.out.println("Twilio Message SID: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Twilio Error: " + e.getMessage());
            System.out.println("NOTE: SMS failed, but you can still use the OTP from the console above.");
        }
    }

    /**
     * Validates the OTP provided by the user against the one in cache.
     */
    public boolean validateOtp(String phone, String otp) {
        if (phone == null || otp == null) {
            return false;
        }
        
        String cachedOtp = otpCache.get(phone);
        boolean isValid = cachedOtp != null && cachedOtp.equals(otp);

        if (isValid) {
            // Remove OTP from cache after successful validation (one-time use)
            otpCache.remove(phone);
        }
        
        return isValid;
    }
}
