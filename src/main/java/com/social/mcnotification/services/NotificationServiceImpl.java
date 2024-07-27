package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.exceptions.SettingsAlreadyCreatedException;
import com.social.mcnotification.kafka.service.KafkaMessageService;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.services.helper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UUID id = UUID.randomUUID(); //test

//    @Value("${app.kafka.MessageTopic}")
//    private String topicName;

    private NotificationRepository notificationRepository;
    private NotificationSettingRepository notificationSettingRepository;
    private Mapper mapper;
    Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationSettingDto getNotificationSettings() {
        logger.log(Level.INFO, "setting up notifications for the user: {}");
        return mapper.mapToNotificationSettingDto(notificationSettingRepository.findById(id));
    }

    @Override
    public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(id);

        switch (notificationUpdateDto.getNotificationType()) {
            case LIKE -> notificationSettingEntity.setEnableLike(notificationUpdateDto.isEnable());
            case POST -> notificationSettingEntity.setEnablePost(notificationUpdateDto.isEnable());
            case POST_COMMENT -> notificationSettingEntity.setEnablePostComment(notificationUpdateDto.isEnable());
            case COMMENT_COMMENT -> notificationSettingEntity.setEnableCommentComment(notificationUpdateDto.isEnable());
            case MESSAGE -> notificationSettingEntity.setEnableMessage(notificationUpdateDto.isEnable());
            case FRIEND_REQUEST -> notificationSettingEntity.setEnableFriendRequest(notificationUpdateDto.isEnable());
            case FRIEND_BIRTHDAY -> notificationSettingEntity.setEnableFriendBirthday(notificationUpdateDto.isEnable());
            case SEND_EMAIL_MESSAGE ->
                    notificationSettingEntity.setEnableSendEmailMessage(notificationUpdateDto.isEnable());
        }

        notificationSettingRepository.save(notificationSettingEntity);

        logger.log(Level.INFO, "Update notification settings for user: {}");

    }

    @Override
    public void markAllEventsAsRead() {
        logger.log(Level.INFO, "all notifications for user: {} are marked as read");
        List<NotificationEntity> notificationEntities = notificationRepository.findById(id);
        notificationEntities.forEach(notification -> notification.setIsReaded(true));
        notificationRepository.saveAll(notificationEntities);


    }

    @Override
    public Boolean createNotificationSettings(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        logger.log(Level.INFO, "create a notification setting for the user: {}", id);

        NotificationSettingDto notificationSetting = new NotificationSettingDto();
        notificationSetting.setId(id);
        notificationSettingRepository.save(mapper.mapToSettingEntity(notificationSetting));
        return true;
    }

    @Override
    public void createNotification(EventNotificationDto eventNotificationDto) {
        logger.log(Level.INFO, "Event created");
        NotificationEntity notification = new NotificationEntity();
        notification.setId(id);
        notification.setAuthorId(eventNotificationDto.getAuthorId());
        notification.setReceiverId(eventNotificationDto.getReceiverId());
        notification.setNotificationType(eventNotificationDto.getNotificationType());
        notification.setContent(eventNotificationDto.getContent());
        notificationRepository.save(notification);

    }

    @Override
    public PageNotificationsDto getNotifications(int page, int size, List<String> sort, Pageable pageable) {
        logger.log(Level.INFO, "Events received for user with id: {}");
        return null;
    }

    @Override
    public NotificationCountDto getEventsCount() {
        List<NotificationEntity> notifications = notificationRepository.findById(id);
        Count count = new Count(notifications.size());
        logger.log(Level.INFO, "Count notifications for the user: {}");
        return new NotificationCountDto(LocalDateTime.now(), count);
    }


}
