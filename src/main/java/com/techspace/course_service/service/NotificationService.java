package com.techspace.course_service.service;

import java.util.List;

import com.techspace.course_service.entity.Notification;

public interface NotificationService {

    Notification createNotification(Notification notification);

    List<Notification> getUserNotifications(Long userId);

    Notification markAsRead(Long id);

    long getUnreadCount(Long userId);
}