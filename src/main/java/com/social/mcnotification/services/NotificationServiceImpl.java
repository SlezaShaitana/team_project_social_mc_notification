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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
    @Service
    @RequiredArgsConstructor
    public class NotificationServiceImpl implements NotificationService {

        private final NotificationRepository notificationRepository;
        private final NotificationSettingRepository notificationSettingRepository;
        private final Mapper mapper;
        private final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

        public UserModel getCurrentUser() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();

            System.out.println("Principal type: " + principal.getClass().getName());

            System.out.println("Principal details: " + principal);

            if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
                return (UserModel) authentication.getPrincipal();
            }
            throw new IllegalStateException("Current user is not of type UserModel");
        }


        @Override
        public NotificationSettingDto getNotificationSettings() {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Getting notification settings for user: {}", user.getId());
//
//            NotificationSettingEntity settingEntity = notificationSettingRepository.findById(user.getId());

            NotificationSettingEntity settingEntity = notificationSettingRepository.findByUserId(user.getId());
            if (settingEntity == null) {
                throw new NotificationSettingNotFoundException("Notification settings not found for user: " + user.getId());
            }

            return mapper.mapToNotificationSettingDto(settingEntity);
        }

        @Override
        public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Updating notification settings for user: {}", user.getId());

//            NotificationSettingEntity settingEntity = notificationSettingRepository.findById(user.getId());

            NotificationSettingEntity settingEntity = notificationSettingRepository.findByUserId(user.getId());


            if (settingEntity == null) {
                throw new NotificationSettingNotFoundException("Notification settings not found for user: " + user.getId());
            }

            if (notificationUpdateDto.getNotificationType() == null) {
                throw new InvalidNotificationTypeException("Notification type is not specified");
            }
            if (notificationUpdateDto.getEnable() == null) {
                throw new InvalidNotificationSettingException("Notification setting is not specified");
            }

            Boolean setting = notificationUpdateDto.getEnable();
            switch (notificationUpdateDto.getNotificationType()) {
                case POST -> settingEntity.setEnablePost(setting);
                case POST_COMMENT -> settingEntity.setEnablePostComment(setting);
                case COMMENT_COMMENT -> settingEntity.setEnableCommentComment(setting);
                case MESSAGE -> settingEntity.setEnableMessage(setting);
                case FRIEND_REQUEST -> settingEntity.setEnableFriendRequest(setting);
                case FRIEND_BIRTHDAY -> settingEntity.setEnableFriendBirthday(setting);
                case SEND_EMAIL_MESSAGE -> settingEntity.setEnableSendEmailMessage(setting);
                default -> throw new InvalidNotificationTypeException("Unknown notification type: " + notificationUpdateDto.getNotificationType());
            }

            notificationSettingRepository.save(settingEntity);
            logger.log(Level.INFO, "Notification settings updated for user: {} to {}", user.getId(), setting);
        }

        @Override
        public void markAllEventsAsRead() {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Marking all notifications as read for user: {}", user.getId());

            List<NotificationEntity> notifications = notificationRepository.findByAuthorId(user.getId());
            if (notifications.isEmpty()) {
                throw new NotificationNotFoundException("No notifications found for user: " + user.getId());
            }

            notifications.forEach(notification -> notification.setIsReaded(true));
            notificationRepository.saveAll(notifications);
        }

        @Override
        public Boolean createNotificationSettings(UUID id) {
            if (id == null) {
                throw new IllegalArgumentException("ID cannot be null");
            }
            logger.log(Level.INFO, "Creating notification settings for user: {}", id);

            NotificationSettingDto notificationSettingDto = new NotificationSettingDto();
            notificationSettingDto.setUserId(id);
            NotificationSettingEntity settingEntity = mapper.mapToSettingEntity(notificationSettingDto);
            notificationSettingRepository.save(settingEntity);
            return true;
        }

        @Override
        public void createNotification(EventNotificationDto eventNotificationDto) {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Creating event notification for user: {}", user.getId());

            if (eventNotificationDto == null) {
                throw new NotificationNotFoundException("EventNotificationDto is null");
            }

            NotificationEntity notification = new NotificationEntity();
            notification.setId(UUID.randomUUID());
            notification.setSentTime(Timestamp.valueOf(LocalDateTime.now()));
            notification.setAuthorId(user.getId());
            notification.setReceiverId(eventNotificationDto.getReceiverId());
            notification.setNotificationType(eventNotificationDto.getNotificationType());
            notification.setContent(eventNotificationDto.getContent());
            notification.setIsReaded(false);
            notificationRepository.save(notification);
        }

        @Override
        public Page<NotificationEntity> getNotifications(Integer page, Integer size, List<String> sort) {
            UserModel user = getCurrentUser();

            System.out.println("id " + user.getId() + "\n" + "token " + user.getToken()
                    + "\n" + "email " + user.getEmail() + "\n" + "roles" + user.getRoles());

            logger.log(Level.INFO, "Fetching notifications for user: {}", user.getId());

            Sort sortObj = Sort.by(Sort.Order.desc("sentTime"));
            if (sort != null && !sort.isEmpty()) {
                sortObj = Sort.by(sort.stream()
                        .map(s -> s.startsWith("-") ? Sort.Order.desc(s.substring(1)) : Sort.Order.asc(s))
                        .toArray(Sort.Order[]::new));
            }

            Specification<NotificationEntity> spec = Specification.where(NotificationsSpecifications.byAuthorId(user.getId()));
            Pageable pageable = PageRequest.of(page, size, sortObj);

            return notificationRepository.findAll(spec, pageable);
        }

        @Override
        public NotificationCountDto getEventsCount() {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Counting events for user: {}", user.getId());

            List<NotificationEntity> notifications = notificationRepository.findByReceiverId(user.getId());
            if (notifications.isEmpty()) {
                throw new NotificationNotFoundException("No notifications found for user: " + user.getId());
            }

            long unreadCount = notifications.stream()
                    .filter(notification -> !notification.getIsReaded())
                    .count();

            Count count = new Count((int) unreadCount);
            return new NotificationCountDto(LocalDateTime.now(), count);
        }
    }
