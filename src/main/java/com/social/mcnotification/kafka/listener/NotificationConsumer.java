package com.social.mcnotification.kafka.listener;

import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.kafka.service.KafkaMessageService;
import com.social.mcnotification.repository.NotificationRepository;
import com.social.mcnotification.services.NotificationServiceImpl;
import com.social.mcnotification.services.helper.Mapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final KafkaMessageService kafkaMessageService;

    @KafkaListener(topics = "${app.kafka.kafkaMessageTopic}",
            groupId = "${app.kafka.kafkaMessageGroupId}",
            containerFactory = "kafkaMessageConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload NotificationDto notificationDto,
                       @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
        log.info("Received notification: {}", notificationDto);
        log.info("Key: {}; Partition: {}; Topic: {}; Timestamp {}", key, partition, topic, timestamp);

        kafkaMessageService.savingToNotificationRepository(notificationDto);

    }
}