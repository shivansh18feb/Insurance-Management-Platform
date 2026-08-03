package com.insurance.backend.controller;

import com.insurance.backend.dto.response.ApiResponse;
import com.insurance.backend.dto.response.NotificationResponseDto;
import com.insurance.backend.repository.UserRepository;
import com.insurance.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    // ─── Authenticated-user endpoints (used by frontend) ─────────────────────

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getMyNotifications(
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.<List<NotificationResponseDto>>builder()
                .success(true)
                .message("Notifications fetched")
                .data(notificationService.getUserNotifications(userId))
                .build());
    }

    @GetMapping("/my/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getMyUnread(
            Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.<List<NotificationResponseDto>>builder()
                .success(true)
                .message("Unread notifications fetched")
                .data(notificationService.getUnreadNotifications(userId))
                .build());
    }

    @GetMapping("/my/unread-count")
    public ResponseEntity<ApiResponse<Long>> getMyUnreadCount(Authentication authentication) {
        Long userId = getUserId(authentication);
        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .success(true)
                .message("Unread count fetched")
                .data(notificationService.getUnreadCount(userId))
                .build());
    }

    @PutMapping("/my/read-all")
    public ResponseEntity<ApiResponse<String>> markAllRead(Authentication authentication) {
        Long userId = getUserId(authentication);
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("All notifications marked as read")
                .data(null)
                .build());
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Notification marked as read")
                .data(null)
                .build());
    }

    // ─── Admin endpoints (by userId) ─────────────────────────────────────────

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationResponseDto>>> getUserNotifications(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.<List<NotificationResponseDto>>builder()
                .success(true)
                .message("Notifications fetched")
                .data(notificationService.getUserNotifications(userId))
                .build());
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.<Long>builder()
                .success(true)
                .message("Unread count fetched")
                .data(notificationService.getUnreadCount(userId))
                .build());
    }

    // ─── Helper ──────────────────────────────────────────────────────────────

    private Long getUserId(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new com.insurance.backend.exception.ResourceNotFoundException("User not found"))
                .getId();
    }
}
