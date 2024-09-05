package com.social.mcnotification.kafka.listener;

import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.RegistrationDto;
import com.social.mcnotification.kafka.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopic}", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload NotificationDto notificationDto) {
        log.info("Received notification: {}", notificationDto);
//        ExecutorService executorService = new DelegatingSecurityContextExecutorService(Executors.newSingleThreadExecutor());
        try {
//            executorService.submit(() -> kafkaMessageService.savingToNotificationRepository(notificationDto));
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
