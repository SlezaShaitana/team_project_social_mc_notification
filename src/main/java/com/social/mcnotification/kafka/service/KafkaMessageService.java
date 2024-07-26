package com.social.mcnotification.kafka.service;

import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.services.helper.Mapper;
import lombok.RequiredArgsConstructor;
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

    private final List<NotificationDto> messages = new ArrayList<>();

    public void savingToNotificationRepository(NotificationDto notificationDto) {
        notificationRepository.save(mapper.mapToNotificationEntity(notificationDto));
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
