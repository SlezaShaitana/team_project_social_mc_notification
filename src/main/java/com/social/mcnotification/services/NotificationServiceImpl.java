package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
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

    private final UUID id = UUID.randomUUID(); //test
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
    Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationSettingDto getNotificationSettings() {
        logger.log(Level.INFO, "setting up notifications for the user: {}");
        return mapper.mapToNotificationSettingDto(notificationSettingRepository.findById(id));
    }

    @Override
    public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
        logger.log(Level.INFO, "Update notification settings for user: {}");
        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(id);

        switch (notificationUpdateDto.getNotificationType()) {
            case LIKE -> notificationSettingEntity.setEnableLike(notificationUpdateDto.isEnable());
            case POST -> notificationSettingEntity.setEnablePost(notificationUpdateDto.isEnable());
            case POST_COMMENT -> notificationSettingEntity.setEnablePostComment(notificationUpdateDto.isEnable());
            case COMMENT_COMMENT -> notificationSettingEntity.setEnableCommentComment(notificationUpdateDto.isEnable());
            case MESSAGE -> notificationSettingEntity.setEnableMessage(notificationUpdateDto.isEnable());
            case FRIEND_REQUEST -> notificationSettingEntity.setEnableFriendRequest(notificationUpdateDto.isEnable());
            case FRIEND_BIRTHDAY -> notificationSettingEntity.setEnableFriendBirthday(notificationUpdateDto.isEnable());
            case SEND_EMAIL_MESSAGE -> notificationSettingEntity.setEnableSendEmailMessage(notificationUpdateDto.isEnable());
        }

        notificationSettingRepository.save(notificationSettingEntity);

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


    public List<NotificationDto> getNotificationDtoList() {
        List<NotificationEntity> notifications = notificationRepository.findById(id);
        return notifications.stream()
                .map(notificationEntity -> mapper.mapToNotificationDto(notificationEntity))
                .toList();
    }

    public List<NotificationsDto> getNotificationsDtoList() {
        List<NotificationDto> notificationDtoList = getNotificationDtoList();
        return notificationDtoList.stream()
                .map(notification -> new NotificationsDto(notification.getSentTime(), notification)).toList();
    }

    public PageableObject getPageableObject(Sort sortCriteria, int size) {
        PageableObject pageableObject = new PageableObject();
        pageableObject.setOffset(offset);
        pageableObject.setSort(sortCriteria);
        pageableObject.setPaged(true);
        pageableObject.setPageSize(size);
        pageableObject.setUnpaged(true);
        pageableObject.setPageNumber(number);
        return pageableObject;
    }

    public Sort getSortCriteria(List<String> sort) {
        return new Sort(
                Boolean.parseBoolean(sort.get(0)),
                Boolean.parseBoolean(sort.get(0)),
                Boolean.parseBoolean(sort.get(0))); // ???
    }

    public PageNotificationsDto getPageNotificationsDto(int size, List<NotificationsDto> notificationsDtoSublist, Sort sortCriteria) {
        PageNotificationsDto pageNotificationsDto = new PageNotificationsDto();
        pageNotificationsDto.setTotalPages(totalPages);
        pageNotificationsDto.setTotalElements(getNotificationsDtoList().size());
        pageNotificationsDto.setNumber(number);
        pageNotificationsDto.setSize(size);
        pageNotificationsDto.setContent(notificationsDtoSublist);
        pageNotificationsDto.setSort(sortCriteria);
        pageNotificationsDto.setFirst(number == 0);
        pageNotificationsDto.setLast(number == totalPages);
        pageNotificationsDto.setNumberOfElements(
                notificationsDtoSublist.size() - LIMIT == 0 ? LIMIT : notificationsDtoSublist.size() - LIMIT);

        PageableObject pageableObject = getPageableObject(sortCriteria, notificationsDtoSublist.size());
        pageNotificationsDto.setPageable(pageableObject);
        pageNotificationsDto.setEmpty(notificationsDtoList.isEmpty());
        return pageNotificationsDto;
    }


    @Override
    public PageNotificationsDto getNotifications(int page, int size, List<String> sort, Pageable pageable) {
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
        List<NotificationEntity> notifications = notificationRepository.findById(id);
        List<NotificationEntity> unreadNotifications = notifications.stream()
                .filter(notification -> !notification.getIsReaded() || notification.getIsReaded() == null)
                .toList();

        Count count = new Count(unreadNotifications.size());
        logger.log(Level.INFO, "Count notifications for the user: {}");
        return new NotificationCountDto(LocalDateTime.now(), count);
    }


}
