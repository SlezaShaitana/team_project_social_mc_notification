package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotificationUpdateDto {
    private boolean enable;
    private final NotificationType notificationType;
}
