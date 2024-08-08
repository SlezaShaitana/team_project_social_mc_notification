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
import com.social.mcnotification.security.jwt.JwtUtils;
import com.social.mcnotification.security.jwt.User;
import com.social.mcnotification.services.helper.Mapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JwtTokenFilter jwtTokenFilter;
    private final String token = jwtTokenFilter.getUser().getToken();
    private final User user = jwtTokenFilter.getUser();
    private final UUID id = user.getId();
    private final int LIMIT = 10;
    private int offset;
    private List<NotificationsDto> notificationsDtoList = new ArrayList<>();
    private int totalPages;
    private int number;

//    @Value("${app.kafka.MessageTopic}")
//    private String topicName;

    private NotificationRepository notificationRepository;
    private NotificationSettingRepository notificationSettingRepository;
    private Mapper mapper;
    private final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationSettingDto getNotificationSettings() {
        logger.log(Level.INFO, "setting up notifications for the user: {}", id);
        NotificationSettingDto notificationSettingDto = mapper.mapToNotificationSettingDto(notificationSettingRepository.findById(id));
        if (notificationSettingDto == null) {
            throw new NotificationSettingNotFoundException("Notification settings not found for user: " + id);
        }
        return mapper.mapToNotificationSettingDto(notificationSettingRepository.findById(id));
    }

    @Override
    public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
        logger.log(Level.INFO, "Update notification settings for user: {}", id);
        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(id);
        Boolean setting = notificationUpdateDto.getEnable();
        if (notificationSettingEntity == null) {
            throw new NotificationSettingNotFoundException("Notification settings not found for user: " + id);
        }
        if (notificationUpdateDto.getNotificationType() == null) {
            throw new InvalidNotificationTypeException("Notification type is not specified");
        }
        if (notificationUpdateDto.getEnable() == null){
            throw new InvalidNotificationSettingException("Notification setting is not specified");
        }

        switch (notificationUpdateDto.getNotificationType()) {
            case LIKE -> { notificationSettingEntity.setEnableLike(setting);
            logger.log(Level.INFO, "Обновлены настройки уведомлений для лайков для пользователя: {} на {}", id, setting);}
            case POST -> { notificationSettingEntity.setEnablePost(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для постов для пользователя: {} на {}", id, setting);}
            case POST_COMMENT -> { notificationSettingEntity.setEnablePostComment(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для комментариев к постам для пользователя: {} на {}", id, setting);}
            case COMMENT_COMMENT -> { notificationSettingEntity.setEnableCommentComment(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для комментариев к комментариям для пользователя: {} на {}", id, setting);}
            case MESSAGE -> { notificationSettingEntity.setEnableMessage(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для сообщений для пользователя: {} на {}", id, setting);}
            case FRIEND_REQUEST -> { notificationSettingEntity.setEnableFriendRequest(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для запросов в друзья для пользователя: {} на {}", id, setting);}
            case FRIEND_BIRTHDAY -> { notificationSettingEntity.setEnableFriendBirthday(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для дней рождения друзей для пользователя: {} на {}", id, setting);}
            case SEND_EMAIL_MESSAGE -> { notificationSettingEntity.setEnableSendEmailMessage(setting);
                logger.log(Level.INFO, "Обновлены настройки уведомлений для отправки сообщений по электронной почте для пользователя: {} на {}", id, setting);}
        }
        notificationSettingRepository.save(notificationSettingEntity);
    }

    @Override
    public void markAllEventsAsRead() {
        logger.log(Level.INFO, "all notifications for user: {} are marked as read", id);
        List<NotificationEntity> notificationEntities = notificationRepository.findById(id);
        if (notificationEntities.isEmpty()) {
            throw new NotificationNotFoundException("Notification not found for user: " + id);
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
        logger.log(Level.INFO, "Event created");
        if (eventNotificationDto == null) {
            throw new NotificationNotFoundException("EventNotificationDto is null");
        }
        NotificationEntity notification = new NotificationEntity();
        notification.setId(id);
        notification.setAuthorId(eventNotificationDto.getAuthorId());
        notification.setReceiverId(eventNotificationDto.getReceiverId());
        notification.setNotificationType(eventNotificationDto.getNotificationType());
        notification.setContent(eventNotificationDto.getContent());
        notificationRepository.save(notification);
    }

//
//    public List<NotificationDto> getNotificationDtoList() {
//        List<NotificationEntity> notifications = notificationRepository.findById(id);
//        return notifications.stream()
//                .map(notificationEntity -> mapper.mapToNotificationDto(notificationEntity))
//                .toList();
//    }
//
//    public List<NotificationsDto> getNotificationsDtoList() {
//        List<NotificationDto> notificationDtoList = getNotificationDtoList();
//        return notificationDtoList.stream()
//                .map(notification -> new NotificationsDto(notification.getSentTime(), notification)).toList();
//    }
//
//    public PageableObject getPageableObject(Sort sortCriteria, int size) {
//        PageableObject pageableObject = new PageableObject();
//        pageableObject.setOffset(offset);
//        pageableObject.setSort(sortCriteria);
//        pageableObject.setPaged(true);
//        pageableObject.setPageSize(size);
//        pageableObject.setUnpaged(true);
//        pageableObject.setPageNumber(number);
//        return pageableObject;
//    }
//
//    public Sort getSortCriteria(List<String> sort) {
//        return new Sort(
//                Boolean.parseBoolean(sort.get(0)),
//                Boolean.parseBoolean(sort.get(0)),
//                Boolean.parseBoolean(sort.get(0))); // ???
//    }

//    public PageNotificationsDto getPageNotificationsDto(int size, List<NotificationsDto> notificationsDtoSublist, Sort sortCriteria) {
//        PageNotificationsDto pageNotificationsDto = new PageNotificationsDto();
//        pageNotificationsDto.setTotalPages(totalPages);
//        pageNotificationsDto.setTotalElements(getNotificationsDtoList().size());
//        pageNotificationsDto.setNumber(number);
//        pageNotificationsDto.setSize(size);
//        pageNotificationsDto.setContent(notificationsDtoSublist);
//        pageNotificationsDto.setSort(sortCriteria);
//        pageNotificationsDto.setFirst(number == 0);
//        pageNotificationsDto.setLast(number == totalPages);
//        pageNotificationsDto.setNumberOfElements(
//                notificationsDtoSublist.size() - LIMIT == 0 ? LIMIT : notificationsDtoSublist.size() - LIMIT);
//
//        PageableObject pageableObject = getPageableObject(sortCriteria, notificationsDtoSublist.size());
//        pageNotificationsDto.setPageable(pageableObject);
//        pageNotificationsDto.setEmpty(notificationsDtoList.isEmpty());
//        return pageNotificationsDto;
//    }


    @Override
    public PageNotificationsDto getNotifications(int page, int size, List<String> sort) {
        Sort sortCriteria = getSortCriteria(sort);
        if (notificationsDtoList == null) {
            logger.log(Level.INFO, "Events received for user with id: {}");
            notificationsDtoList = getNotificationsDtoList();
            int totalElements = notificationsDtoList.size();
            totalPages =  (int) Math.ceil((double) totalElements / LIMIT);
        }

        List<NotificationsDto> notificationsDtoSublist = notificationsDtoList.subList(offset, Math.min(offset + LIMIT, notificationsDtoList.size()));
        offset += LIMIT;

        PageNotificationsDto pageNotificationsDto = getPageNotificationsDto(size, notificationsDtoSublist, sortCriteria);
        number ++;

        return pageNotificationsDto;
    }


    @Override
    public NotificationCountDto getEventsCount() {
        logger.log(Level.INFO, "Count notifications for the user: {}", id);
        List<NotificationEntity> notifications = notificationRepository.findById(id);
        if (notifications.isEmpty()) {
            throw new NotificationNotFoundException("Notifications not found for user: " + id);
        }
        List<NotificationEntity> unreadNotifications = notifications.stream()
                .filter(notification -> !notification.getIsReaded())
                .toList();

        Count count = new Count(unreadNotifications.size());
        return new NotificationCountDto(LocalDateTime.now(), count);
    }


}
