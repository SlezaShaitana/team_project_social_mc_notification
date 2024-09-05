package com.social.mcnotification.kafka.service;

import com.social.mcnotification.client.AuthClient;
import com.social.mcnotification.client.FriendClient;
import com.social.mcnotification.client.dto.AuthenticateDto;
import com.social.mcnotification.client.dto.AuthenticateResponseDto;
import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.dto.RegistrationDto;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.services.helper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaMessageService {
    private final Mapper mapper;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final FriendClient friendClient;
    private final List<NotificationDto> messages = new ArrayList<>();
    private final String email = "sasha@gmail.com";
    private final String password = "abc";
    private final AuthClient authClient;

    //Смотреть откуда пришло
    //Пример: пришла из постов
    //принимаешь это сообщение
    //смотришь друзей этого пользователя --> friends
    //сохраняешь в БД столько уведомлений, сколько у пользователя друзей, меняя толкьо receiverId
    public void savingToNotificationRepository(NotificationDto notificationDto) {
        log.info("method savingToNotificationRepository");
        log.info("Microservice {}, type {}, authorId {}, receiverId {}, isReaded {}", notificationDto.getServiceName(),
                notificationDto.getNotificationType(),
                notificationDto.getAuthorId(),
                notificationDto.getReceiverId(),
                notificationDto.getIsReaded());

        switch (notificationDto.getServiceName()) {
            case POST -> setNotificationMessageForPostMicroservice(notificationDto);
            case DIALOG -> setNotificationMessageForDialogMicroservice(notificationDto);
            case FRIENDS -> setNotificationMessageForFriendMicroservice(notificationDto);
            case ACCOUNT -> setNotificationMessageForAccountMicroservice(notificationDto);
        }
    }

    public NotificationEntity createNotification(NotificationDto notificationDto) {
        NotificationEntity notification = new NotificationEntity();
        notification.setAuthorId(notificationDto.getAuthorId());
        notification.setReceiverId(notificationDto.getReceiverId());
        notification.setNotificationType(notificationDto.getNotificationType());
        notification.setServiceName(notificationDto.getServiceName());
        notification.setContent(notificationDto.getContent());
        notification.setIsReaded(false);
        notification.setSentTime(Timestamp.valueOf(LocalDateTime.now()));
        notification.setEventId(notificationDto.getEventId());

        return notification;
    }

    public boolean userWantsNotification(NotificationDto notificationDto, NotificationType type) {
        boolean userWantsNotType = false;
        NotificationSettingDto setting = mapper.mapToNotificationSettingDto(notificationSettingRepository.findByUserId(notificationDto.getReceiverId()));

        if (setting != null) {
            switch (type.toString()) {
                case "POST" -> userWantsNotType = setting.isEnablePost();
                case "POST_COMMENT" -> userWantsNotType = setting.isEnablePostComment();
                case "COMMENT_COMMENT" -> userWantsNotType = setting.isEnableCommentComment();
                case "FRIEND_REQUEST" -> userWantsNotType = setting.isEnableFriendRequest();
                case "FRIEND_REQUEST_CONFIRMATION" -> userWantsNotType = setting.isEnableFriendRequest();
                case "MESSAGE" -> userWantsNotType = setting.isEnableMessage();
                case "FRIEND_BIRTHDAY" -> userWantsNotType = setting.isEnableFriendBirthday();
                case "SEND_EMAIL_MESSAGE" -> userWantsNotType = setting.isEnableSendEmailMessage();
                case "LIKE_POST" -> userWantsNotType = true;
                case "LIKE_COMMENT" -> userWantsNotType = true;
            }
        }
        if (!userWantsNotType) {
            log.info("User does not want to receive notifications of type: {}", notificationDto.getNotificationType());
        }
        return userWantsNotType;

    }

    public void notifyAllFriends(NotificationDto notificationDto) {
//         for type:
//         FRIEND_BIRTHDAY
//         POST
        String token = login();

        if (notificationDto.getReceiverId() == null) {
            ResponseEntity<List<UUID>> response = friendClient.getFriendsIdListByUserId(token, notificationDto.getAuthorId());
            List<UUID> listFriendsId = response.getBody();

            if (listFriendsId != null) {
                for (UUID uuid : listFriendsId) {
                    notificationDto.setReceiverId(uuid);
                    if (notificationDto.getNotificationType() == NotificationType.FRIEND_BIRTHDAY) {
                        notificationDto.setContent("Сегодня у вашего друга день рождения! Не забудьте поздравить!");
                    }
                    notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
                }
            }

        }
    }



    public void setNotificationMessageForPostMicroservice(NotificationDto notificationDto) {
        if (userWantsNotification(notificationDto,notificationDto.getNotificationType())) {
            if (notificationDto.getNotificationType() == NotificationType.POST) {
                notifyAllFriends(notificationDto); //получают все друзья автора поста
            }
            NotificationEntity notification = createNotification(notificationDto);
            notificationRepository.save(notification);
        }
    }

    public void setNotificationMessageForAccountMicroservice(NotificationDto notificationDto) {
        if (userWantsNotification(notificationDto, notificationDto.getNotificationType())) {
            notifyAllFriends(notificationDto);
        }
    }

    // type MESSAGE
    public void setNotificationMessageForDialogMicroservice(NotificationDto notificationDto) {
        if (userWantsNotification(notificationDto, notificationDto.getNotificationType())) {
            // получит только тот кому отправили сообщение
            NotificationEntity notification = createNotification(notificationDto);
            notification.setIsReaded(false);
            notificationRepository.save(notification);
        }
    }

    public void setNotificationMessageForFriendMicroservice(NotificationDto notificationDto) {
        if (userWantsNotification(notificationDto, notificationDto.getNotificationType())) {
            NotificationEntity notification = createNotification(notificationDto);
            notification.setIsReaded(false);
            notificationRepository.save(notification);
        }
    }

    public void setNotificationMessageForAuthMicroservice(RegistrationDto registrationDto) {
        NotificationSettingEntity entity = new NotificationSettingEntity();
        entity.setUserId(registrationDto.getUuid());
        notificationSettingRepository.save(entity);
//        NEW_USER_REGISTRATION
//        notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
    }

    public void addToList(NotificationDto notificationDto) {
        messages.add(notificationDto);
    }

//    public boolean findById(List<NotificationSettingDto> settingsDtoList, UUID id) {
//        Optional<NotificationSettingDto> settingOptional = settingsDtoList.stream()
//                .filter(setting -> setting.getId().equals(id))
//                .findFirst();
//
//        return settingOptional.isPresent();
//    }

    public Optional<NotificationDto> findById(UUID id) {
        return messages.stream().filter(not -> not.getId().equals(id)).findFirst();
    }

    public String login() {
        ResponseEntity<AuthenticateResponseDto> response = authClient.login(new AuthenticateDto(email, password));
        AuthenticateResponseDto authenticateResponseDto = response.getBody();
        return authenticateResponseDto.getAccessToken();
    }
}
