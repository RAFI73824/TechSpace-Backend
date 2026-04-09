package com.techspace.course_service.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.entity.User;
import com.techspace.course_service.service.StudentProfileServiceImpl;

@RestController
@RequestMapping("/api/profile")
public class StudentProfileController {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileController.class);

    @Autowired
    private StudentProfileServiceImpl profileService;

    // Validate phone number format (must be exactly 10 digits)
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[0-9]{10}$";  // Regex for exactly 10 digits
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();  // Returns true if phone number matches the pattern
    }

    // Validate email format
    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";  // Regex for valid email
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();  // Returns true if email matches the pattern
    }

    // Endpoint to get user profile information
    @GetMapping("/{userId}")
    public User getUserProfile(@PathVariable Long userId) {
        logger.info("Fetching profile for user ID: " + userId);
        return profileService.getUserProfile(userId);
    }

    // Endpoint to update user profile information (name, email, phone)
    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId, @RequestBody User updatedUser) {
        logger.info("Updating profile for user ID: " + userId);
        
        // Validate the phone number before updating
        if (!isValidPhoneNumber(updatedUser.getPhone())) {
            logger.error("Invalid phone number: " + updatedUser.getPhone());
            return ResponseEntity.badRequest().body("Invalid phone number. It must be exactly 10 digits.");
        }

        // Validate the email
        if (!isValidEmail(updatedUser.getEmail())) {
            logger.error("Invalid email address: " + updatedUser.getEmail());
            return ResponseEntity.badRequest().body("Invalid email address. Please provide a valid email.");
        }

        // If validation passes, update the profile
        User updated = profileService.updateUserProfile(userId, updatedUser);
        logger.info("Successfully updated profile for user ID: " + userId);
        return ResponseEntity.ok("Profile updated successfully");
    }
}