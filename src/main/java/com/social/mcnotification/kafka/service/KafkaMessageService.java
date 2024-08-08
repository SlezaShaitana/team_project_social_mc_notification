package com.social.mcnotification.kafka.service;

import com.social.mcnotification.client.FriendClient;
import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.repository.NotificationRepository;
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
    private final FriendClient friendClient;
    private final List<NotificationDto> messages = new ArrayList<>();

    public void savingToNotificationRepository(NotificationDto notificationDto) {
        MicroServiceName microServiceName = notificationDto.getServiceName();
        //Смотреть откуда пришло

        //Пример: пришла из постов
        //принимаешь это сообщение
        //смотришь друзей этого пользователя --> friends
        //сохраняешь в БД столько уведомлений, сколько у пользователя друзей, меняя толкьо receiverId


        ResponseEntity<List<UUID>> response = friendClient.getFriendsIdListByUserId(notificationDto.getAuthorId());
        List<UUID> listFriendsId = response.getBody();

        if (listFriendsId != null) {
            for (UUID uuid : listFriendsId) {
                notificationDto.setReceiverId(uuid);
                notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
            }
        }
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