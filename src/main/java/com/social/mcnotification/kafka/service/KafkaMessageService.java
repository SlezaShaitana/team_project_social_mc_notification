package com.social.mcnotification.kafka.service;

import com.social.mcnotification.client.AccountClient;
import com.social.mcnotification.client.FriendClient;
import com.social.mcnotification.client.dto.AccountDataDTO;
import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.dto.RegistrationDto;
import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationSettingEntity;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.repository.NotificationSettingRepository;
import com.social.mcnotification.security.SecurityContextHolderStrategyHelper;
import com.social.mcnotification.security.jwt.UserModel;
import com.social.mcnotification.services.NotificationServiceImpl;
import com.social.mcnotification.services.helper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaMessageService {

    //    private static final Logger log = LoggerFactory.getLogger(KafkaMessageService.class);
    private Mapper mapper;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final FriendClient friendClient;
    private final AccountClient accountClient;
    private final List<NotificationDto> messages = new ArrayList<>();

    private final NotificationServiceImpl notificationService;

    @Async("taskExecutor")
    public void savingToNotificationRepository(NotificationDto notificationDto) {
        log.info("method savingToNotificationRepository");
        //Смотреть откуда пришло

        //Пример: пришла из постов
        //принимаешь это сообщение
        //смотришь друзей этого пользователя --> friends
        //сохраняешь в БД столько уведомлений, сколько у пользователя друзей, меняя толкьо receiverId

        //
        SecurityContext securityContext = SecurityContextHolderStrategyHelper.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserModel userModel = (UserModel) authentication.getPrincipal();
        log.info("id {} token {} email {}", userModel.getId(), userModel.getToken(), userModel.getEmail());

        //


        NotificationType type = notificationDto.getNotificationType();
//        UserModel userModel = notificationService.getCurrentUser();
//        log.info("id {} token {} email {}", userModel.getId(), userModel.getToken(), userModel.getEmail());

        AccountDataDTO accountDataDTO = accountClient.getDataMyAccountById(userModel.getToken(), notificationDto.getAuthorId());
        String nameAuthor = " " + accountDataDTO.getFirstName() + " " + accountDataDTO.getLastName();

        switch (notificationDto.getServiceName()) {
            case POST -> setNotificationMessageForPostMicroservice(type, notificationDto, nameAuthor);
            case DIALOG -> setNotificationMessageForDialogMicroservice(notificationDto, nameAuthor);
            case FRIENDS -> setNotificationMessageForFriendMicroservice(type, notificationDto, nameAuthor);
            case ACCOUNT -> setNotificationMessageForAccountMicroservice(notificationDto, nameAuthor);
        }

    }

    public boolean userWantsNotification(NotificationType type) {
        boolean userWantsNotType = false;
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        NotificationSettingDto setting = mapper.mapToNotificationSettingDto(notificationSettingRepository.findByUserId(userModel.getId()));

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
            }
        }
        return userWantsNotType;

    }

    public void notifyAllFriends(NotificationDto notificationDto, String nameAuthor) {
        // для типов уведомлений:
        // FRIEND_BIRTHDAY
        // POST

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserModel userModel = (UserModel) authentication.getPrincipal();

        if (notificationDto.getReceiverId() == null) {
            ResponseEntity<List<UUID>> response = friendClient.getFriendsIdListByUserId(userModel.getToken(), notificationDto.getAuthorId());
            List<UUID> listFriendsId = response.getBody();

            if (listFriendsId != null) {
                for (UUID uuid : listFriendsId) {
                    notificationDto.setReceiverId(uuid);
                    if (notificationDto.getNotificationType() == NotificationType.FRIEND_BIRTHDAY) {
                        notificationDto.setContent("Сегодня день рождения у вашего друга " + nameAuthor + "Не забудьте поздравить!");
                    }
                    notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
                }
            }
        }

    }

    public void setNotificationMessageForPostMicroservice(NotificationType type, NotificationDto notificationDto, String nameAuthor) {
        boolean shouldBeSaved = false;
        if (userWantsNotification(type)) {
            if (type == NotificationType.POST) {
                notifyAllFriends(notificationDto, nameAuthor); //получают все друзья автора поста
            }
            shouldBeSaved = true;
        }
        if (type == NotificationType.LIKE_POST || type == NotificationType.LIKE_COMMENT) { // получит только автор
            shouldBeSaved = true;
        }
        if (shouldBeSaved) {
            notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
        }

    }

    public void setNotificationMessageForAccountMicroservice(NotificationDto notificationDto, String nameAuthor) {
        if (userWantsNotification(notificationDto.getNotificationType())) {
            notifyAllFriends(notificationDto, nameAuthor);
        } else {
            log.info("User does not want to receive notifications of type: {}", notificationDto.getNotificationType());
        }
    }

    // type MESSAGE
    public void setNotificationMessageForDialogMicroservice(NotificationDto notificationDto, String nameAuthor) {
        if (userWantsNotification(notificationDto.getNotificationType())) {
//            notificationDto.setContent("Пользователь " + nameAuthor + " написал вам сообщение");
            // получит только тот кому отправили сообщение
            notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
        } else {
            log.info("User does not want to receive notifications of type: {}", notificationDto.getNotificationType());
        }
    }

    public void setNotificationMessageForFriendMicroservice(NotificationType type, NotificationDto notificationDto, String nameAuthor) {
        if (userWantsNotification(type) || type == NotificationType.FRIEND_REQUEST_CONFIRMATION) {
            notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
        } else {
            log.info("User does not want to receive notifications of type: {}", type);
        }
    }

    public void setNotificationMessageForAuthMicroservice(RegistrationDto registrationDto) {
        NotificationSettingEntity entity = new NotificationSettingEntity();
        entity.setUserId(registrationDto.getUuid());
        notificationSettingRepository.save(entity);
        //зарегестророван новый пользователь
        //NEW_USER_REGISTRATION
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
}
