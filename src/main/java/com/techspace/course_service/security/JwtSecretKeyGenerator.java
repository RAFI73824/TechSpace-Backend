package com.techspace.course_service.security;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtSecretKeyGenerator {

    public static void main(String[] args) {
        // SecureRandom to generate a strong random number
        SecureRandom secureRandom = new SecureRandom();

        // Array to hold 32 bytes of data (256 bits)
        byte[] secretKey = new byte[32]; // 32 bytes = 256 bits
        secureRandom.nextBytes(secretKey);  // Generate random bytes

        // Encode the byte array into a Base64 string for easy storage
        String secretKeyBase64 = Base64.getEncoder().encodeToString(secretKey);

        // Print the generated secret key
        System.out.println("Generated JWT Secret Key: " + secretKeyBase64);
    }
}