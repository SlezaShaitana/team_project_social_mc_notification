package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.exceptions.InvalidNotificationSettingException;
import com.social.mcnotification.exceptions.InvalidNotificationTypeException;
import com.social.mcnotification.exceptions.NotificationNotFoundException;
import com.social.mcnotification.exceptions.NotificationSettingNotFoundException;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.security.jwt.JwtTokenFilter;
import com.social.mcnotification.security.jwt.UserModel;
import com.social.mcnotification.services.helper.Mapper;
import com.social.mcnotification.specifications.NotificationsSpecifications;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JwtTokenFilter jwtTokenFilter;
//    private final UUID id = jwtTokenFilter.getUser().getId();


//    @Value("${app.kafka.MessageTopic}")
//    private String topicName;

    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private Mapper mapper;
    private final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationSettingDto getNotificationSettings() {
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.log(Level.INFO, "setting up notifications for the user: {}", user.getId());

        NotificationSettingDto notificationSettingDto = mapper.mapToNotificationSettingDto(notificationSettingRepository.findById(user.getId()));
        if (notificationSettingDto == null) {
            throw new NotificationSettingNotFoundException("Notification settings not found for user: " + user.getId());
        }
        return notificationSettingDto;
    }

    @Override
    public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.log(Level.INFO, "Update notification settings for user: {}", user.getId());
        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(user.getId());
        Boolean setting = notificationUpdateDto.getEnable();
        if (notificationSettingEntity == null) {
            throw new NotificationSettingNotFoundException("Notification settings not found for user: " + user.getId());
        }
        if (notificationUpdateDto.getNotificationType() == null) {
            throw new InvalidNotificationTypeException("Notification type is not specified");
        }
        if (notificationUpdateDto.getEnable() == null) {
            throw new InvalidNotificationSettingException("Notification setting is not specified");
        }

        switch (notificationUpdateDto.getNotificationType()) {
            case POST -> {
                notificationSettingEntity.setEnablePost(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для постов. {} на {}", user.getId(), setting);
            }
            case POST_COMMENT -> {
                notificationSettingEntity.setEnablePostComment(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для комментариев к постам. {} на {}", user.getId(), setting);
            }
            case COMMENT_COMMENT -> {
                notificationSettingEntity.setEnableCommentComment(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для комментариев к комментариям. {} на {}", user.getId(), setting);
            }
            case MESSAGE -> {
                notificationSettingEntity.setEnableMessage(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для сообщений. {} на {}", user.getId(), setting);
            }
            case FRIEND_REQUEST -> {
                notificationSettingEntity.setEnableFriendRequest(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для запросов в друзья. {} на {}", user.getId(), setting);
            }
            case FRIEND_BIRTHDAY -> {
                notificationSettingEntity.setEnableFriendBirthday(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для дней рождения друзей. {} на {}", user.getId(), setting);
            }
            case SEND_EMAIL_MESSAGE -> {
                notificationSettingEntity.setEnableSendEmailMessage(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для отправки сообщений по электронной почте. {} на {}", user.getId(), setting);
            }
        }
        notificationSettingRepository.save(notificationSettingEntity);
    }

    @Override
    public void markAllEventsAsRead() {
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.log(Level.INFO, "setting up notifications for the user: {}", user.getId());
        List<NotificationEntity> notificationEntities = notificationRepository.findByAuthorId(user.getId());
        if (notificationEntities.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found for user: " + user.getId());
        }
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
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.log(Level.INFO, "Event created");
        if (eventNotificationDto == null) {
            throw new NotificationNotFoundException("EventNotificationDto is null");
        }
        NotificationEntity notification = new NotificationEntity();
        notification.setId(UUID.randomUUID());
        notification.setAuthorId(user.getId());
        notification.setReceiverId(eventNotificationDto.getReceiverId());
        notification.setNotificationType(eventNotificationDto.getNotificationType());
        notification.setContent(eventNotificationDto.getContent());
        notificationRepository.save(notification);
    }

    @Override
    public Page<NotificationEntity> getNotifications(Integer page, Integer size, List<String> sort) {
//        org.springframework.data.domain.Sort sortObj = Sort.by(sort.get(0), sort.get(1), sort.get(2));
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sort sortObj = Sort.unsorted();

        Specification<NotificationEntity> spec = Specification.where(null);
        spec = spec.and(NotificationsSpecifications.byAuthorId(user.getId()));

        Pageable pageableDto = PageRequest.of(page, size, Sort.by("sentTime").descending());

        return notificationRepository.findAll(spec, pageableDto);
    }


    @Override
    public NotificationCountDto getEventsCount() {
        UserModel user = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.log(Level.INFO, "setting up notifications for the user: {}", user.getId());
        List<NotificationEntity> notifications = notificationRepository.findByAuthorId(user.getId());
        if (notifications.isEmpty()) {
            throw new NotificationNotFoundException("Notifications not found for user: " + user.getId());
        }
        List<NotificationEntity> unreadNotifications = notifications.stream()
                .filter(notification -> !notification.getIsReaded())
                .toList();

        Count count = new Count(unreadNotifications.size());
        return new NotificationCountDto(LocalDateTime.now(), count);
    }

}
