package com.social.mcnotification.kafka.listener;

import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.kafka.service.KafkaMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//@Component
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopic}", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload NotificationDto notificationDto) {
        log.info("Received notification: {}", notificationDto);

        try {
            kafkaMessageService.savingToNotificationRepository(notificationDto);
            log.info("Notification successfully saved: {}", notificationDto);
        } catch (Exception e) {
            log.error("Error saving notification: {}", notificationDto, e);
        }

    }

//    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopicAuth}", groupId = "${spring.kafka.kafkaMessageGroupIdAuth}", containerFactory = "authKafkaMessageConcurrentKafkaListenerContainerFactory")
//    public void listenAuth(@Payload RegistrationDto registrationDto) {
//        log.info("Received registration: {}", registrationDto);
//
//        kafkaMessageService.setNotificationMessageForAuthMicroservice(registrationDto);
//    }


//    @KafkaListener(topics = "registerTopic", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "authkKafkaMessageConcurrentKafkaListenerContainerFactory")
//    public void listen(RegistrationDto accountDtoRequest) {
//        log.info("Received data: " + accountDtoRequest);
//        try {
//            kafkaMessageService.setNotificationMessageForAuthMicroservice(accountDtoRequest);
//
//        } catch (Exception e) {
//            log.error("Failed to create account from Kafka message", e);
//        }
//    }
//
//    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopic}", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "authkKafkaMessageConcurrentKafkaListenerContainerFactory")
//    public void listen(NotificationDto notificationDto) {
//        log.info("Received data: " + notificationDto);
//        try {
//            kafkaMessageService.savingToNotificationRepository(notificationDto);
//
//        } catch (Exception e) {
//            log.error("Failed to notification from Kafka message", e);
//        }
//    }


//    @KafkaListener(topics = "${spring.kafka.kafkaMessageTopicAuth}", groupId = "${spring.kafka.kafkaMessageGroupId}", containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
//    public void listenAuth(@Payload RegistrationDto registrationDto,
//                           @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
//                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
//                           @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
//                           @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
//        log.info("Received registration: {}", registrationDto);
//        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp {}", key, partition, topic, timestamp);
//
//        kafkaMessageService.setNotificationMessageForAuthMicroservice(registrationDto);
//    }


}
