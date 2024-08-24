package com.social.mcnotification.services.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
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
