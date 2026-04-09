package com.techspace.course_service.service;

import com.techspace.course_service.entity.User;

public interface AuthService {
    User processGoogleUser(String email, String name);
    User processPhoneUser(String phone);
    User authenticateUser(String email, String password);
    User registerUser(User user);
}