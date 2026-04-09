package com.techspace.course_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techspace.course_service.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);

    boolean existsByUserIdAndCourseIdAndType(Long userId, Long courseId, String type);
}