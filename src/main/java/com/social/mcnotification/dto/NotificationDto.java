package com.social.mcnotification.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDto {
    @JsonProperty("id")
    private UUID id;
    private UUID authorId;
    @JsonProperty("content")
    private String content;
    private NotificationType notificationType;
    @JsonProperty("sentTime")
    private Timestamp sentTime; //Timestamp
    private UUID receiverId;
    @JsonProperty("serviceName")
    private MicroServiceName serviceName;
    @JsonProperty("eventId")
    private UUID eventId;
    @JsonProperty("isReaded")
    private Boolean isReaded;

    public NotificationDto(NotificationEntity notificationEntity) {
        this.id = notificationEntity.getId();
        this.authorId = notificationEntity.getAuthorId();
        this.content = notificationEntity.getContent();
        this.notificationType = notificationEntity.getNotificationType();
        this.sentTime = notificationEntity.getSentTime();
        this.receiverId = notificationEntity.getReceiverId();
        this.serviceName = notificationEntity.getServiceName();
        this.eventId = notificationEntity.getEventId();
        this.isReaded = notificationEntity.getIsReaded();
    }
}
