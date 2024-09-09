package com.social.mcnotification.services;

import com.social.mcnotification.dto.*;
import com.social.mcnotification.dto.response.PageNotificationsDto;
import com.social.mcnotification.dto.response.PageableObject;
import com.social.mcnotification.exceptions.InvalidNotificationTypeException;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.security.jwt.UserModel;
import com.social.mcnotification.services.helper.Mapper;
import com.social.mcnotification.specifications.NotificationsSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final Mapper mapper;
//        private final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);


    public UserModel getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
            return (UserModel) authentication.getPrincipal();
        }
        throw new IllegalStateException("Current user is not of type UserModel");
    }

    @Override
    public NotificationSettingDto getNotificationSettings() {
        UserModel user = getCurrentUser();
        log.info("Getting notification settings for user: {}", user.getId());

        NotificationSettingEntity settingEntity = notificationSettingRepository.findByUserId(user.getId());
        if (settingEntity == null) {
            log.info("Notification settings not found for user: " + user.getId());
            settingEntity = new NotificationSettingEntity();
            settingEntity.setUserId(getCurrentUser().getId());
            settingEntity.setEnableLike(true);
            settingEntity.setEnablePostComment(true);
            settingEntity.setEnableFriendRequest(true);
            settingEntity.setEnableCommentComment(true);
            settingEntity.setEnableFriendBirthday(true);
            settingEntity.setEnablePost(true);
            settingEntity.setEnableMessage(true);
            notificationSettingRepository.save(settingEntity);
        }
        return mapper.mapToNotificationSettingDto(settingEntity);
    }

    @Override
    public void updateNotificationSettings(NotificationUpdateDto notificationUpdateDto) {
        UserModel user = getCurrentUser();
        log.info("Updating notification settings for user: {}", user.getId());
        NotificationSettingEntity settingEntity = notificationSettingRepository.findByUserId(user.getId());
        Boolean setting = notificationUpdateDto.getEnable();
        switch (notificationUpdateDto.getNotificationType()) {
            case POST -> settingEntity.setEnablePost(setting);
            case POST_COMMENT -> settingEntity.setEnablePostComment(setting);
            case COMMENT_COMMENT -> settingEntity.setEnableCommentComment(setting);
            case MESSAGE -> settingEntity.setEnableMessage(setting);
            case FRIEND_REQUEST -> settingEntity.setEnableFriendRequest(setting);
            case FRIEND_BIRTHDAY -> settingEntity.setEnableFriendBirthday(setting);
            case SEND_EMAIL_MESSAGE -> settingEntity.setEnableSendEmailMessage(setting);
            default ->
                    throw new InvalidNotificationTypeException("Unknown notification type: " + notificationUpdateDto.getNotificationType());
        }
        notificationSettingRepository.save(settingEntity);
        log.info("Notification settings updated for user: {} to {}", user.getId(), setting);
    }

    @Override
    public void markAllEventsAsRead() {
        UserModel user = getCurrentUser();
        log.info("Marking all notifications as read for user: {}", user.getId());
        List<NotificationEntity> notifications = notificationRepository.findByReceiverId(user.getId());
        if (notifications.isEmpty()) {
            log.info("No notifications found for user: {} ", user.getId());
        } else {
            notifications.forEach(notification -> notification.setIsReaded(true));
            notificationRepository.saveAll(notifications);
        }
        notificationRepository.deleteAll(notifications);
    }

    @Override
    public Boolean createNotificationSettings(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        log.info("Creating notification settings for user: {}", id);
        NotificationSettingDto notificationSettingDto = new NotificationSettingDto();
        notificationSettingDto.setUserId(id);
        notificationSettingDto.setEnableLike(true);
        notificationSettingDto.setEnablePostComment(true);
        notificationSettingDto.setEnableFriendRequest(true);
        notificationSettingDto.setEnableCommentComment(true);
        notificationSettingDto.setEnableFriendBirthday(true);
        notificationSettingDto.setEnablePost(true);
        notificationSettingDto.setEnableMessage(true);
        NotificationSettingEntity settingEntity = mapper.mapToSettingEntity(notificationSettingDto);
        notificationSettingRepository.save(settingEntity);
        return true;
    }

    @Override
    public void createNotification(EventNotificationDto eventNotificationDto) {
        UserModel user = getCurrentUser();
        log.info("Creating event notification for user: {}", user.getId());

        if (eventNotificationDto == null) {
            log.info("EventNotificationDto is null");
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

    @Override
    public PageNotificationsDto getNotifications(Integer page, Integer size, String sort, String headerRequestByAuth) {
        UserModel user = getCurrentUser();
        log.info("get Notification");
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        String direction = sortParts[1];
        Sort sortObj = Sort.by("desc".equalsIgnoreCase(direction) ? Sort.Order.desc(field) : Sort.Order.asc(field));

        Specification<NotificationEntity> spec = Specification.where(NotificationsSpecifications.byReceiverId(user.getId()));
        spec = spec.and(NotificationsSpecifications.isReaded(false));

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<NotificationEntity> pageNotifications = notificationRepository.findAll(spec, pageable);
        log.info("Прочитано {} событий", pageNotifications.get().count());
        NotificationsDto[] content = getNotificationsDtos(pageNotifications);
        PageableObject pageableObject = getPageableObject(pageable, sortObj);
        return getPageNotificationsDto(pageable, pageNotifications, content, pageableObject, sortObj);
    }

    private static PageNotificationsDto getPageNotificationsDto(Pageable page, Page<NotificationEntity> pageNotifications, NotificationsDto[] content, PageableObject pageableObject, Sort sortObj) {
        return PageNotificationsDto.builder()
                .totalPages(pageNotifications.getTotalPages())
                .totalElements((int) pageNotifications.get().count())
                .number(page.getPageNumber())
                .size(page.getPageSize())
                .content(content)
                .sort(new SortDto(sortObj.isEmpty(), sortObj.isUnsorted(), sortObj.isSorted()))
                .first(pageNotifications.isFirst())
                .last(pageNotifications.isLast())
                .numberOfElements(pageNotifications.getNumberOfElements())
                .pageable(pageableObject)
                .empty(pageNotifications.isEmpty())
                .build();
    }

    private static PageableObject getPageableObject(Pageable pageable, Sort sort) {
        SortDto sortDTO = SortDto.builder()
                .unsorted(sort.isUnsorted())
                .sorted(sort.isSorted())
                .empty(sort.isEmpty())
                .build();

        return PageableObject.builder()
                .sortDto(sortDTO)
                .unpaged(pageable.isUnpaged())
                .paged(pageable.isPaged())
                .pageSize(pageable.getPageSize())
                .pageNumber(pageable.getPageNumber())
                .offset((int) pageable.getOffset())
                .build();
    }

    private static NotificationsDto[] getNotificationsDtos(Page<NotificationEntity> pageNotifications) {

        List<NotificationsDto> list = new ArrayList<>();

        pageNotifications.forEach(x -> {
            NotificationDto notificationDto = NotificationDto.builder()
                    .id(x.getId())
                    .authorId(x.getAuthorId())
                    .content(x.getContent())
                    .notificationType(x.getNotificationType())
                    .sentTime(x.getSentTime())
                    .build();

            NotificationsDto notificationsDto = new NotificationsDto();
            notificationsDto.setTimeStamp(x.getSentTime());
            notificationsDto.setData(notificationDto);

            list.add(notificationsDto);
        });

        NotificationsDto[] content = list.toArray(new NotificationsDto[0]);
        return content;
    }

    @Override
    public NotificationCountDto getEventsCount() {
        UserModel user = getCurrentUser();
        log.info("Counting events for user: if {} email {} token {}", user.getId(), user.getEmail(), user.getToken());

        List<NotificationEntity> notifications = notificationRepository.findByReceiverId(user.getId());
        if (notifications.isEmpty()) {
            log.info("No notifications found for user: {} ", user.getId());
        }

        long unreadCount = notifications.stream()
                .filter(notification -> !notification.getIsReaded())
                .count();

        Count count = new Count((int) unreadCount);
        return new NotificationCountDto(LocalDateTime.now(), count);
    }
}
