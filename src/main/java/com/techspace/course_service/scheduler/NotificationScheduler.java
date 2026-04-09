package com.techspace.course_service.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.techspace.course_service.entity.Notification;
import com.techspace.course_service.entity.User;
import com.techspace.course_service.repository.NotificationRepository;
import com.techspace.course_service.repository.UserRepository;

@Component
public class NotificationScheduler {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public NotificationScheduler(UserRepository userRepository,
                                 NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendLearningReminders() {

        List<User> users = userRepository.findAll();

        for (User user : users) {

            Notification notification = Notification.builder()
                    .user(user)
                    .title("Keep Learning 🚀")
                    .message("Continue your course today. Your future is waiting.")
                    .type("LEARNING_REMINDER")
                    .severity("LOW")
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .isRead(false)
                    .build();

            notificationRepository.save(notification);
        }
    }
}