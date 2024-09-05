package com.social.mcnotification.services.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class Mapper {

    public NotificationEntity createNotification(NotificationDto notificationDto) {
        NotificationEntity notification = new NotificationEntity();
        notification.setAuthorId(notificationDto.getAuthorId());
        notification.setReceiverId(notificationDto.getReceiverId());
        notification.setNotificationType(notificationDto.getNotificationType());
        notification.setServiceName(notificationDto.getServiceName());
        notification.setContent(notificationDto.getContent());
        notification.setIsReaded(false);
        notification.setSentTime(Timestamp.valueOf(LocalDateTime.now()));
        notification.setEventId(notificationDto.getEventId());

        return notification;
    }

    public NotificationEntity mapToNotificationEntity(NotificationDto notificationDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(notificationDto, NotificationEntity.class);
    }

    public NotificationDto mapToNotificationDto(NotificationEntity notificationEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(notificationEntity, NotificationDto.class);
    }

    public NotificationSettingEntity mapToSettingEntity(NotificationSettingDto notificationSettingDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(notificationSettingDto, NotificationSettingEntity.class);
    }

    public NotificationSettingDto mapToNotificationSettingDto(NotificationSettingEntity notificationSettingEntity) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(notificationSettingEntity, NotificationSettingDto.class);
    }
}
