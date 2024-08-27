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
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
    @RequiredArgsConstructor
    @Slf4j
    public class NotificationServiceImpl implements NotificationService {

        private final NotificationRepository notificationRepository;
        private final NotificationSettingRepository notificationSettingRepository;
        private final Mapper mapper;
        private final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

        private void checkPrintUserInfo(UserModel user) {
            System.out.println("id " + user.getId() +
                    "\n" + "token " + user.getToken() +
                    "\n" + "email " + user.getEmail() +
                    "\n" + "roles" + user.getRoles());
        }

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

            NotificationSettingEntity settingEntity = notificationSettingRepository.findByUserId(user.getId());
            if (settingEntity == null) {
                logger.log(Level.ERROR, "Notification settings not found for user: " + user.getId());
            }

            return mapper.mapToNotificationSettingDto(settingEntity);
        }

        @Override
        public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Updating notification settings for user: {}", user.getId());

            NotificationSettingEntity settingEntity = notificationSettingRepository.findByUserId(user.getId());

            if (settingEntity == null) {
                log.error("Notification settings not found for user: {} ", user.getId());
                settingEntity = new NotificationSettingEntity();
                settingEntity.setUserId(getCurrentUser().getId());
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

            List<NotificationEntity> notifications = notificationRepository.findByReceiverId(user.getId());
            if (notifications.isEmpty()) {
                logger.log(Level.INFO, "No notifications found for user: {} ", user.getId());
//                throw new NotificationNotFoundException("No notifications found for user: " + user.getId());
            } else {
                notifications.forEach(notification -> notification.setIsReaded(true));
                notificationRepository.saveAll(notifications);
            }
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
                logger.log(Level.INFO, "EventNotificationDto is null");
            } else {
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
        }


//    public Page<NotificationsDto> getNotifications(Integer page, Integer size, List<String> sort) {
//        UserModel user = getCurrentUser();
//
//        logger.log(Level.INFO, "Fetching notifications for user: {}", user.getId());
//
//        Sort sortObj = Sort.by(Sort.Order.desc("sentTime"));
//        if (sort != null && !sort.isEmpty()) {
//            sortObj = Sort.by(sort.stream()
//                    .map(s -> s.startsWith("-") ? Sort.Order.desc(s.substring(1)) : Sort.Order.asc(s))
//                    .toArray(Sort.Order[]::new));
//        }
//
//        Specification<NotificationEntity> spec = Specification.where(NotificationsSpecifications.byReceiverId(user.getId()));
//        Pageable pageable = PageRequest.of(page, size, sortObj);
//
//        Page<NotificationEntity> notificationEntities = notificationRepository.findAll(spec, pageable);
//
//        List<NotificationDto> notificationsDtos = notificationEntities.getContent().stream()
//                .map(this::convertToNotificationsDto)
//                .collect(Collectors.toList());
//
//        NotificationsDto n = new NotificationsDto();
//        n.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
//        n.setData(notificationsDtos);
//
//        return new PageImpl<>(List.of(n), pageable, notificationEntities.getTotalElements());
//    }
//
//    private NotificationDto convertToNotificationsDto(NotificationEntity entity) {
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setId(entity.getId());
//        notificationDto.setAuthorId(entity.getAuthorId());
//        notificationDto.setContent(entity.getContent());
//        notificationDto.setNotificationType(entity.getNotificationType());
//        notificationDto.setSentTime(entity.getSentTime());
//        notificationDto.setReceiverId(entity.getReceiverId());
//        notificationDto.setServiceName(entity.getServiceName());
//        notificationDto.setEventId(entity.getEventId());
//        notificationDto.setIsReaded(entity.getIsReaded());
//
//
//        return notificationDto;
//    }


//    @Override
//    public PageImpl<NotificationsDto> getNotifications(Integer page, Integer size, List<String> sort) {
//        UserModel user = getCurrentUser();
//
//        Sort sortObj = Sort.by(Sort.Order.desc("sentTime"));
//        if (sort != null && !sort.isEmpty()) {
//            sortObj = Sort.by(sort.stream()
//                    .map(s -> s.startsWith("-") ? Sort.Order.desc(s.substring(1)) : Sort.Order.asc(s))
//                    .toArray(Sort.Order[]::new));
//        }
//
//        Specification<NotificationEntity> spec = Specification.where(NotificationsSpecifications.byReceiverId(user.getId()));
//        Pageable pageable = PageRequest.of(page, size, sortObj);
//
//        Page<NotificationEntity> notificationPage = notificationRepository.findAll(spec, pageable);
//
//        NotificationsDto notificationsDtos = new NotificationsDto();
//        notificationsDtos.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
//        notificationsDtos.setData(notificationPage.getContent().stream()
//                .map(mapper::mapToNotificationDto).collect(Collectors.toList()));
//
//
//        return new PageImpl<>(List.of(notificationsDtos), pageable, notificationPage.getTotalElements());
//    }








        @Override
        public Page<NotificationEntity> getNotifications(Integer page, Integer size, List<String> sort) {
            UserModel user = getCurrentUser();

            checkPrintUserInfo(user);

            logger.log(Level.INFO, "Fetching notifications for user: {}", user.getId());

            Sort sortObj = Sort.by(Sort.Order.desc("sentTime"));
            if (sort != null && !sort.isEmpty()) {
                sortObj = Sort.by(sort.stream()
                        .map(s -> s.startsWith("-") ? Sort.Order.desc(s.substring(1)) : Sort.Order.asc(s))
                        .toArray(Sort.Order[]::new));
            }

            Specification<NotificationEntity> spec = Specification.where(NotificationsSpecifications.byReceiverId(user.getId()));
            Pageable pageable = PageRequest.of(page, size, sortObj);

            return notificationRepository.findAll(spec, pageable);
        }

        @Override
        public NotificationCountDto getEventsCount() {
            UserModel user = getCurrentUser();
            logger.log(Level.INFO, "Counting events for user: {}", user.getId());

            List<NotificationEntity> notifications = notificationRepository.findByReceiverId(user.getId());
            if (notifications.isEmpty()) {
                logger.log(Level.INFO, "No notifications found for user: {} ", user.getId());
            }

            long unreadCount = notifications.stream()
                    .filter(notification -> !notification.getIsReaded())
                    .count();

            Count count = new Count((int) unreadCount);
            return new NotificationCountDto(LocalDateTime.now(), count);
        }
    }
