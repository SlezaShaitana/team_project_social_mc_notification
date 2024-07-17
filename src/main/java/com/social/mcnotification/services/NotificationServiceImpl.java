package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;

import java.util.UUID;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public NotificationSettingDto getNotificationSettings() {
        return null;
    }

    @Override
    public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {

    }

    @Override
    public void markAllEventsAsRead() {

    }

    @Override
    public void createNotificationSettings(UUID id) {

    }

    @Override
    public void createEvent(EventNotificationDto eventNotificationDto) {

    }

    @Override
    public PageNotificationsDto getEvents(Pageable page) {
        return null;
    }

    @Override
    public NotificationCountDto getEventsCount() {
        return null;
    }
}
