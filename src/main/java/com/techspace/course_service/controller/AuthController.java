package com.techspace.course_service.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.techspace.course_service.dto.*;
import com.techspace.course_service.entity.User;
import com.techspace.course_service.repository.UserRepository;
import com.techspace.course_service.security.JwtService;
import com.techspace.course_service.service.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private TwilioService twilioService;
    @Autowired private EmailService emailService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        twilioService.sendOtp(request.get("phone"));
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    @PostMapping("/phone-login")
    public ResponseEntity<AuthResponse> phoneLogin(@RequestBody PhoneLoginRequest request) {
        if (!twilioService.validateOtp(request.getPhone(), request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }
        User user = authService.processPhoneUser(request.getPhone());
        return generateAuthResponse(user);
    }

    @PostMapping("/email-login")
    public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> request) {
        User user = authService.authenticateUser(request.get("email"), request.get("password"));
        return generateAuthResponse(user);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Map<String, String> request) {
        User user = new User();
        user.setEmail(request.get("email"));
        user.setPassword(request.get("password"));
        user.setName(request.get("name"));
        user.setPhone(request.get("phone"));
        user = authService.registerUser(user);
        return generateAuthResponse(user);
    }

   @PostMapping("/google")
public ResponseEntity<AuthResponse> googleLogin(@RequestBody Map<String, String> request) {

    String token = request.get("token");

    try {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                new GsonFactory()
        )
        .setAudience(Collections.singletonList(
            "1093719290286-uc5lora2h8eq3pt412m7lm5iq25gpmoo.apps.googleusercontent.com"
        ))
        .build();

        GoogleIdToken idToken = verifier.verify(token);

        if (idToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // Your existing logic
        User user = authService.processGoogleUser(email, name);

        return generateAuthResponse(user);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "User not registered. Kindly register first."));
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(System.currentTimeMillis() + 15 * 60 * 1000);
        userRepository.save(user);

        // Update this URL to your Render Frontend URL
        String resetLink = "https://techspace-frontend.onrender.com/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);

        return ResponseEntity.ok(Map.of("message", "Reset link sent to your email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {
        User user = userRepository.findByResetToken(req.get("token"))
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        user.setPassword(passwordEncoder.encode(req.get("newPassword")));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }

    private ResponseEntity<AuthResponse> generateAuthResponse(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail() != null ? user.getEmail() : user.getPhone())
                .password("")
                .authorities("ROLE_STUDENT")
                .build();
        String token = jwtService.generateToken(userDetails);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user.getName());
        userMap.put("email", user.getEmail());
        userMap.put("phone", user.getPhone());
        return ResponseEntity.ok(new AuthResponse(token, userMap));
    }
}
