package com.social.mcnotification.controller;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class ApiController {

    private final NotificationService notificationService;

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
    public ResponseEntity<String> markAllEventsAsRead() {
        notificationService.markAllEventsAsRead();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/settings{id}")
    public ResponseEntity<Boolean> createNotificationSettings(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(notificationService.createNotificationSettings(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createNotification(@RequestBody EventNotificationDto eventNotificationDto) {
        notificationService.createNotification(eventNotificationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/notifications")
    public ResponseEntity<PageNotificationsDto> getNotifications(@RequestParam("page") int page,
                                                                 @RequestParam("size") int size,
                                                                 @RequestParam("sort") List<String> sort,
                                                                 Pageable pageable) {
        return ResponseEntity.ok(notificationService.getNotifications(page, size, sort, pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<NotificationCountDto> getEventsCount() {
        return ResponseEntity.ok(notificationService.getEventsCount());
    }
}
