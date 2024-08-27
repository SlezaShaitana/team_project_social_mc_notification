package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.model.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationSettingDto getNotificationSettings();

    void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto);

    void markAllEventsAsRead();

    Boolean createNotificationSettings(UUID id);

    void createNotification(EventNotificationDto eventNotificationDto);

    Page<NotificationEntity> getNotifications(Integer page, Integer size, List<String> sort);

//    Page<NotificationsDto> getNotifications(Integer page, Integer size, List<String> sort);

    NotificationCountDto getEventsCount();
}
