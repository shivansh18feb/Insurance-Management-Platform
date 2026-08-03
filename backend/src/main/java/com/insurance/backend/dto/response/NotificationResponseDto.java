package com.insurance.backend.dto.response;

import com.insurance.backend.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String message;
    private NotificationType notificationType;
    private boolean isRead;
    private Long recipientUserId;
    private LocalDateTime createdAt;
}
