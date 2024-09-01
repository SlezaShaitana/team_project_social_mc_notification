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
import com.social.mcnotification.services.helper.Mapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class KafkaMessageService {

    private Mapper mapper;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final FriendClient friendClient;
    private final AccountClient accountClient;
    private final List<NotificationDto> messages = new ArrayList<>();

    public void savingToNotificationRepository(NotificationDto notificationDto) {
        //Смотреть откуда пришло

        //Пример: пришла из постов
        //принимаешь это сообщение
        //смотришь друзей этого пользователя --> friends
        //сохраняешь в БД столько уведомлений, сколько у пользователя друзей, меняя толкьо receiverId

        MicroServiceName microServiceName = notificationDto.getServiceName();
        NotificationType type = notificationDto.getNotificationType();
        AccountDataDTO accountDataDTO = accountClient.getDataMyAccountById(notificationDto.getAuthorId());
        String nameAuthor = " " + accountDataDTO.getFirstName() + " " + accountDataDTO.getLastName();

        switch (notificationDto.getServiceName()) {
            case POST -> setNotificationMessageForPostMicroservice(type, notificationDto, nameAuthor);
            case DIALOG -> setNotificationMessageForDialogMicroservice(notificationDto, nameAuthor);
            case FRIENDS -> setNotificationMessageForFriendMicroservice(type, notificationDto, nameAuthor);
            case ACCOUNT -> setNotificationMessageForAccountMicroservice(notificationDto, nameAuthor);
        }

    }

    public void notifyAllFriends(NotificationDto notificationDto, String nameAuthor) {
        // для типов уведомлений:
        // FRIEND_BIRTHDAY
        // POST

        if (notificationDto.getReceiverId() == null) {
            ResponseEntity<List<UUID>> response = friendClient.getFriendsIdListByUserId(notificationDto.getAuthorId());
            List<UUID> listFriendsId = response.getBody();

            if (listFriendsId != null) {
                for (UUID uuid : listFriendsId) {
                    notificationDto.setReceiverId(uuid);
                    if (notificationDto.getNotificationType() == NotificationType.FRIEND_BIRTHDAY) {
                        notificationDto.setContent("Сегодня день рождения у вашего друга " + nameAuthor + "Не забудьте поздравить!");
                    }
                    if (notificationDto.getNotificationType() == NotificationType.POST) {
                        notificationDto.setContent("Пользователь " + nameAuthor + " написал новый пост");
                    }
                    notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
                }
            }
        }

    }

    public void setNotificationMessageForPostMicroservice(NotificationType type, NotificationDto notificationDto, String nameAuthor) {
        if (type == NotificationType.POST) {
            //получают все друзья автора поста
            notifyAllFriends(notificationDto, nameAuthor);
        } else if (type == NotificationType.LIKE_POST) {
            // получит только автор поста
            notificationDto.setContent("Пользователь " + nameAuthor + " поставил лайк к посту");

        } else if (type == NotificationType.LIKE_COMMENT) {
            // получит только автор комментария
            notificationDto.setContent("Пользователь " + nameAuthor + " поставил лайк к комментарию");

        } else if (type == NotificationType.POST_COMMENT) {
            // получит только автор поста
            notificationDto.setContent("Пользователь " + nameAuthor + " оставил комментарий под постом");

        } else if (type == NotificationType.COMMENT_COMMENT) {
            // получит только автор комментария
            notificationDto.setContent("Пользователь" + nameAuthor + "прокомментировал комментарий");

        }
        notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
    }

    public void setNotificationMessageForAccountMicroservice(NotificationDto notificationDto, String nameAuthor) {
        notifyAllFriends(notificationDto, nameAuthor);
    }

    // type MESSAGE
    public void setNotificationMessageForDialogMicroservice(NotificationDto notificationDto, String nameAuthor) {
        notificationDto.setContent("Пользователь " + nameAuthor + " написал вам сообщение");
        // получит только тот кому отправили сообщение
        notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
    }

    public void setNotificationMessageForFriendMicroservice(NotificationType type, NotificationDto notificationDto, String nameAuthor) {
        if (type == NotificationType.FRIEND_REQUEST) {
            //получит только тот кому отправлен запрос
            notificationDto.setContent("Вы получили запрос на дружбу от пользователя " + nameAuthor);
        }

        //подтверждение запроса на дружбу
        if (type == NotificationType.FRIEND_REQUEST_CONFIRMATION) {
            notificationDto.setContent(nameAuthor + " теперь ваш друг");
        }
        notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
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

    public static boolean findById(List<NotificationSettingDto> settingsDtoList, UUID id) {
        Optional<NotificationSettingDto> settingOptional = settingsDtoList.stream()
                .filter(setting -> setting.getId().equals(id))
                .findFirst();

        return settingOptional.isPresent();
    }
}
