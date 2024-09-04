package com.social.mcnotification.kafka.listener;

import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.RegistrationDto;
import com.social.mcnotification.kafka.service.KafkaMessageService;
import com.social.mcnotification.security.SecurityContextHolderStrategyHelper;
import com.social.mcnotification.security.jwt.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopic}", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload NotificationDto notificationDto) {
        log.info("Received notification: {}", notificationDto);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        SecurityContextHolderStrategyHelper.setContext(securityContext);

        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("id {} token {} email {}", userModel.getId(), userModel.getToken(), userModel.getEmail());

        try {
            kafkaMessageService.savingToNotificationRepository(notificationDto);
            log.info("Notification successfully saved: {}", notificationDto);
        } catch (Exception e) {
            log.error("Error saving notification: {}", notificationDto, e);
        }

    }

    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopicAuth}", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "authKafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listenAuth(@Payload RegistrationDto registrationDto) {
        log.info("Received notification: {}", registrationDto);

        try {
            kafkaMessageService.setNotificationMessageForAuthMicroservice(registrationDto);
            log.info("Notification successfully saved: {}", registrationDto);
        } catch (Exception e) {
            log.error("Error saving notification: {}", registrationDto, e);
        }

    }
}
