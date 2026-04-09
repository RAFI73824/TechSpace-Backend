package com.techspace.course_service.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.User;
import com.techspace.course_service.exception.InvalidCredentialsException;
import com.techspace.course_service.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= PHONE LOGIN =================
 
    @Override
    public User processPhoneUser(String phone) {

        return userRepository.findByPhone(phone)
                .orElseGet(() -> {

                    User user = new User();

                    user.setPhone(phone); // +91...
                    user.setPhoneVerified(true);
                    user.setEmail(phone + "@techspace.local");
                    user.setRole("STUDENT");
                    user.setName("User_" + phone.substring(phone.length() - 4));

                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

                    return userRepository.save(user);
                });
    }

    // ================= GOOGLE LOGIN =================
    @Override
    public User processGoogleUser(String email, String name) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // update name if placeholder
            if (user.getName() == null ||
                user.getName().equalsIgnoreCase("User") ||
                user.getName().startsWith("Student_")) {

                user.setName(name);
                return userRepository.save(user);
            }

            return user;
        }

        // create new Google user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setRole("STUDENT");
        newUser.setEmailVerified(true);
        newUser.setPhone("G-" + System.currentTimeMillis());
        newUser.setPhoneVerified(false);

        // random password (not used)
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        return userRepository.save(newUser);
    }

    // ================= EMAIL LOGIN =================
    @Override
    public User authenticateUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        // 🚨 prevent wrong login method
        if (user.getPhone() != null && user.getPhone().startsWith("G-")) {
            throw new InvalidCredentialsException("Please login using Google");
        }

        if (user.getEmail() != null && user.getEmail().endsWith("@techspace.local")) {
            throw new InvalidCredentialsException("Please login using phone OTP");
        }

        // password validation
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }

        return user;
    }

    // ================= REGISTER =================
    @Override
    public User registerUser(User user) {

        // check email
        Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("User already exists. Please login.");
        }

        // check phone
        Optional<User> existingPhone = userRepository.findByPhone(user.getPhone());
        if (existingPhone.isPresent()) {
            throw new RuntimeException("Phone already registered.");
        }

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole("STUDENT");
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        return userRepository.save(user);
    }
}