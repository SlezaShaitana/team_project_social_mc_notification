package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationSettingDto getNotificationSettings();

    void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto);

    void markAllEventsAsRead();

    Boolean createNotificationSettings(UUID id);

    void createNotification(EventNotificationDto eventNotificationDto);

    PageNotificationsDto getNotifications(int page, int size, List<String> sort);

    NotificationCountDto getEventsCount();
}
