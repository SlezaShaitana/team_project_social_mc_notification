package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;

import java.util.UUID;

public interface NotificationService {

    NotificationSettingDto getNotificationSettings();

    void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto);

    void markAllEventsAsRead();

    void createNotificationSettings(UUID id);

    void createEvent(EventNotificationDto eventNotificationDto);

    PageNotificationsDto getEvents(Pageable page);

    NotificationCountDto getEventsCount();
}
