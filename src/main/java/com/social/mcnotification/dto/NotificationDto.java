package com.social.mcnotification.dto;

import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
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
    private Timestamp sentTime; //Timestamp
    private UUID receiverId;
    private MicroServiceName serviceName;
    private UUID eventId;
    private Boolean isReaded;



















    public NotificationDto(NotificationEntity notificationEntity) {
        this.id = notificationEntity.getId();
        this.authorId = notificationEntity.getAuthorId();
        this.content = notificationEntity.getContent();
        this.notificationType = notificationEntity.getNotificationType();
//        this.sentTime = notificationEntity.getSentTime().toLocalDateTime();
        this.sentTime = notificationEntity.getSentTime();
        this.receiverId = notificationEntity.getReceiverId();
        this.serviceName = notificationEntity.getServiceName();
        this.eventId = notificationEntity.getEventId();
        this.isReaded = notificationEntity.getIsReaded();
    }
}
