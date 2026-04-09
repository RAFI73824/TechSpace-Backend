package com.techspace.course_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
    Optional<User> findByResetToken(String token);
   
}