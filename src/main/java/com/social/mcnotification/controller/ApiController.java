package com.social.mcnotification.controller;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.services.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class ApiController {

    private final NotificationServiceImpl notificationService;

    @GetMapping("/settings")
    public ResponseEntity<NotificationSettingDto> getNotificationSettings() {
        return ResponseEntity.ok(notificationService.getNotificationSettings());
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateNotificationSettings(@RequestBody NotificationUpdateDto notificationUpdateDto) {
        notificationService.updateNotificationSettings(notificationUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PutMapping("/readed")
    public ResponseEntity<?> markAllEventsAsRead() {
        notificationService.markAllEventsAsRead();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/settings{id}")
    public ResponseEntity<?> createNotificationSettings(@PathVariable("id") UUID id) {
        notificationService.createNotificationSettings(id);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createEvent(@RequestBody EventNotificationDto eventNotificationDto) {
        notificationService.createEvent(eventNotificationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/notifications")
    public ResponseEntity<PageNotificationsDto> getEvents(@RequestParam Pageable page) {
        return ResponseEntity.ok(notificationService.getEvents(page));
    }

    @GetMapping("/count")
    public ResponseEntity<NotificationCountDto> getEventsCount() {
        return ResponseEntity.ok(notificationService.getEventsCount());
    }
}
