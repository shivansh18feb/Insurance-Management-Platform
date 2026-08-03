package com.insurance.backend.service;

import com.insurance.backend.dto.response.NotificationResponseDto;
import com.insurance.backend.entity.enums.NotificationType;

import java.util.List;

public interface NotificationService {
    void createNotification(Long userId, String title, String message, NotificationType type);
    List<NotificationResponseDto> getUserNotifications(Long userId);
    List<NotificationResponseDto> getUnreadNotifications(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    long getUnreadCount(Long userId);
}
