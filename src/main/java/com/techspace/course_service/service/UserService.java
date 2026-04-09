package com.techspace.course_service.service;

import com.techspace.course_service.entity.User;

public interface UserService {

    User registerUser(User user);

    User login(String identifier, String password);

    User updateUser(Long userId, User updatedUser);

    User getUserById(Long userId);

    User getUserByEmail(String email);
}