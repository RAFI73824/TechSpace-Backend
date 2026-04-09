package com.techspace.course_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techspace.course_service.entity.Notification;
import com.techspace.course_service.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    public NotificationServiceImpl(NotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification createNotification(Notification notification) {

        notification.setCreatedAt(LocalDateTime.now());
        notification.setIsRead(false);

        if (notification.getExpiresAt() == null) {
            notification.setExpiresAt(LocalDateTime.now().plusDays(7));
        }

        return repository.save(notification);
    }

    @Override
    public List<Notification> getUserNotifications(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Notification markAsRead(Long id) {

        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);

        return repository.save(notification);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return repository.countByUserIdAndIsReadFalse(userId);
    }
}	