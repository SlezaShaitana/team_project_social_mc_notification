package com.social.mcnotification.controller;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.dto.response.PageNotificationsDto;
import com.social.mcnotification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class ApiController {

    private final NotificationService notificationService;

    @GetMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationSettingDto> getNotificationSettings() {
        return ResponseEntity.ok(notificationService.getNotificationSettings());
    }

    @PutMapping("/settings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateNotificationSettings(@RequestBody NotificationUpdateDto notificationUpdateDto) {
        notificationService.updateNotificationSettings(notificationUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/readed")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> markAllEventsAsRead() {
        notificationService.markAllEventsAsRead();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/settings{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> createNotificationSettings(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(notificationService.createNotificationSettings(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createNotification(@RequestBody EventNotificationDto eventNotificationDto) {
        notificationService.createNotification(eventNotificationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<PageNotificationsDto> getNotifications(@RequestHeader("Authorization") String headerRequestByAuth,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                                 @RequestParam(name = "sort", required = false) String sort) {
        if (page < 0) {
            page = 0;
        }
        if (size < 1) {
            size = 1;
        }
        return ResponseEntity.ok(notificationService.getNotifications(page, size, sort, headerRequestByAuth));
    }

    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationCountDto> getEventsCount() {
        return ResponseEntity.ok(notificationService.getEventsCount());
    }
}
