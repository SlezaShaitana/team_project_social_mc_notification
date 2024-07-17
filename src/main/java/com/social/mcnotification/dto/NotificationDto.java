package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private UUID id;
    private UUID authorId;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime sentTime;

}
