package com.social.mcnotification.dto;

import com.social.mcnotification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotificationUpdateDto {
    private Boolean enable;
    private final NotificationType notificationType;
}
