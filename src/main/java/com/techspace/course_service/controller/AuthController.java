package com.techspace.course_service.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techspace.course_service.dto.AuthResponse;
import com.techspace.course_service.dto.PhoneLoginRequest;
import com.techspace.course_service.entity.User;
import com.techspace.course_service.exception.InvalidCredentialsException;
import com.techspace.course_service.repository.UserRepository;
import com.techspace.course_service.security.JwtService;
import com.techspace.course_service.service.AuthService;
import com.techspace.course_service.service.EmailService;
import com.techspace.course_service.service.TwilioService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TwilioService twilioService;
	@Autowired
	private EmailService emailService;

	@PostMapping("/send-otp")
	public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
		String phone = request.get("phone");
		twilioService.sendOtp(phone);
		return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
	}

	@PostMapping("/phone-login")
	public ResponseEntity<AuthResponse> phoneLogin(@RequestBody PhoneLoginRequest request) {
		// 1. Verify OTP
		if (!twilioService.validateOtp(request.getPhone(), request.getOtp())) {
			throw new InvalidCredentialsException("Invalid OTP");
		}

		// 2. Use Service to get User
		com.techspace.course_service.entity.User user = authService.processPhoneUser(request.getPhone());

		// 3. Create Security Context & Token
		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getPhone())
				.password("").authorities("ROLE_STUDENT").build();

		String token = jwtService.generateToken(userDetails);

		// 4. Build Response
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", user.getName() != null ? user.getName() : "Student");
		userMap.put("phone", user.getPhone());
		userMap.put("role", user.getRole());

		return ResponseEntity.ok(new AuthResponse(token, userMap));
	}

	@PostMapping("/email-login")
	public ResponseEntity<AuthResponse> login(@RequestBody Map<String, String> request) {

		String email = request.get("email");
		String password = request.get("password");

		// Authenticate user
		var user = authService.authenticateUser(email, password);

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()).authorities("ROLE_STUDENT").build();

		String token = jwtService.generateToken(userDetails);

		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", user.getName());
		userMap.put("email", user.getEmail());

		return ResponseEntity.ok(new AuthResponse(token, userMap));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody Map<String, String> request) {

		String email = request.get("email");
		String password = request.get("password");
		String name = request.get("name");
		String phone = request.get("phone");

		com.techspace.course_service.entity.User user = new com.techspace.course_service.entity.User();

		user.setEmail(email);
		user.setPassword(password);
		user.setName(name);
		user.setPhone(phone);

		user = authService.registerUser(user);

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()).authorities("ROLE_STUDENT").build();

		String token = jwtService.generateToken(userDetails);

		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", user.getName());
		userMap.put("email", user.getEmail());

		return ResponseEntity.ok(new AuthResponse(token, userMap));
	}

	@PostMapping("/google")
	public ResponseEntity<AuthResponse> googleLogin(@RequestBody Map<String, String> request) {

		String email = request.get("email");
		String name = request.get("name");

		// ✅ call service
		User user = authService.processGoogleUser(email, name);

		UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().username(user.getEmail())
				.password(user.getPassword()).authorities("ROLE_STUDENT").build();

		String token = jwtService.generateToken(userDetails);

		Map<String, Object> userMap = new HashMap<>();
		userMap.put("name", user.getName());
		userMap.put("email", user.getEmail());

		return ResponseEntity.ok(new AuthResponse(token, userMap));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> req) {

	    String email = req.get("email");

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    String token = UUID.randomUUID().toString();

	    user.setResetToken(token);
	    user.setResetTokenExpiry(System.currentTimeMillis() + 15 * 60 * 1000);

	    userRepository.save(user);

	    // ✅ CREATE RESET LINK
	    String resetLink = "http://localhost:3000/reset-password?token=" + token;

	    // ✅ SEND EMAIL (YOUR EXISTING METHOD)
	    emailService.sendPasswordResetEmail(email, resetLink);

	    return ResponseEntity.ok("Reset link sent to your email");
	}
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> req) {

	    User user = userRepository.findByResetToken(req.get("token"))
	            .orElseThrow(() -> new RuntimeException("Invalid token"));

	    user.setPassword(passwordEncoder.encode(req.get("newPassword")));
	    user.setResetToken(null);
	    user.setResetTokenExpiry(null);

	    userRepository.save(user);

	    return ResponseEntity.ok("Password updated");
	}
}