package com.social.mcnotification.dto;

import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private UUID id;
    private UUID authorId;
    private String content;
    private NotificationType notificationType;
    private LocalDateTime sentTime; //Timestamp
    private UUID receiverId;




    private MicroServiceName serviceName;
    private UUID eventId;
    private Boolean isReaded;

}
