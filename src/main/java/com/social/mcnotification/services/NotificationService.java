package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.dto.response.PageNotificationsDto;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationSettingDto getNotificationSettings();

    void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto);

    void markAllEventsAsRead();

    Boolean createNotificationSettings(UUID id);

    void createNotification(EventNotificationDto eventNotificationDto);

    PageNotificationsDto getNotifications(Integer page, Integer size, String sort, String headerRequestByAuth);

    NotificationCountDto getEventsCount();
}
