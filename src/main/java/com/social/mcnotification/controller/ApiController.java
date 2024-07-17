package com.social.mcnotification.controller;

import com.social.mcnotification.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class ApiController {

    @GetMapping("/settings")
    public ResponseEntity<NotificationSettingDto> getNotificationSettings() {
        return ResponseEntity.ok(null);
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateNotificationSettings(@RequestBody NotificationUpdateDto notificationUpdateDto) {
        return ResponseEntity.ok(null);

    }

    @PutMapping("/readed")
    public ResponseEntity<?> markAllEventsAsRead() {
        return ResponseEntity.ok(null);
    }

    @PostMapping("/settings{id}")
    public ResponseEntity<?> createNotificationSettings(@PathVariable("id") String id) {
        return ResponseEntity.ok(true);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createEvent(@RequestBody EventNotificationDto eventNotificationDto) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/notifications")
    public ResponseEntity<PageNotificationsDto> getEvents(@RequestParam Pageable page) {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getEventsCount() {
        return ResponseEntity.ok(null);
    }
}
