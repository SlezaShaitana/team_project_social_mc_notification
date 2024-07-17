package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class EventNotificationDto {
    private UUID authorId;
    private UUID receiverId;
    private NotificationType notificationType;
    private String content;
}
