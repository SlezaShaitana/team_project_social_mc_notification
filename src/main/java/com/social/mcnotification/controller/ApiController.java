package com.social.mcnotification.controller;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class ApiController {

    private final NotificationService notificationService;

    @GetMapping("/settings")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationSettingDto> getNotificationSettings() {
        return ResponseEntity.ok(notificationService.getNotificationSettings());
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> updateNotificationSettings(@RequestBody NotificationUpdateDto notificationUpdateDto) {
        notificationService.updateNotificationSettings(notificationUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/readed")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<String> markAllEventsAsRead() {
        notificationService.markAllEventsAsRead();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/settings{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<Boolean> createNotificationSettings(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(notificationService.createNotificationSettings(id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> createNotification(@RequestBody EventNotificationDto eventNotificationDto) {
        notificationService.createNotification(eventNotificationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<PageNotificationsDto> getNotifications(@RequestParam(name = "page", defaultValue = "0") int page,
                                                                 @RequestParam(name = "size", defaultValue = "1") int size,
                                                                 @RequestParam(name = "sort", required = true) List<String> sort,
                                                                 Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotifications(page, size, sort, pageable));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationCountDto> getEventsCount() {
        return ResponseEntity.ok(notificationService.getEventsCount());
    }
}
