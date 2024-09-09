//package com.social.mcnotification.kafka;
//
//import com.social.mcnotification.dto.NotificationDto;
//import com.social.mcnotification.enums.MicroServiceName;
//import com.social.mcnotification.enums.NotificationType;
//import com.social.mcnotification.kafka.service.KafkaMessageService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import java.time.Duration;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.await;
//
//
//@SpringBootTest
//@Testcontainers
//@ActiveProfiles("local")
//public class KafkaTestContainersLiveTest {
//
//    @Container
//    static KafkaContainer kafka = new KafkaContainer(
//            DockerImageName.parse("confluentinc/cp-kafka:7.3.3"));
//
//    @DynamicPropertySource
//    static void registryKafkaProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
//    }
//
//    @Autowired
//    private KafkaMessageService kafkaMessageService;
//
//    @Value("${spring.kafka.kafkaMessageTopic}")
//    private String topic;
//
//    @Autowired
//    private KafkaTemplate<String, NotificationDto> kafkaTemplate;
//
//    @Test
//    public void whenSendKafkaMessage_thenHandleMessageByListener() {
//
//        UUID id =  UUID.fromString("aa59a222-5452-41f8-86e9-0d7631621d6d");
//        NotificationDto notificationDto = new NotificationDto();
//        notificationDto.setId(id);
//        notificationDto.setAuthorId(UUID.randomUUID());
//        notificationDto.setNotificationType(NotificationType.MESSAGE);
//        notificationDto.setReceiverId(UUID.randomUUID());
//        notificationDto.setContent("This is a test message");
//        notificationDto.setServiceName(MicroServiceName.DIALOG);
//
//        String key = UUID.randomUUID().toString();
//
//        kafkaTemplate.send(topic, key, notificationDto);
//
//        await()
//                .pollInterval(Duration.ofSeconds(3))
//                .atMost(10, TimeUnit.SECONDS)
//                .untilAsserted(()->{
//                    Optional<NotificationDto> mayBeKafkaNotificationDto = kafkaMessageService.findById(id);
//
//                    assertThat(mayBeKafkaNotificationDto).isPresent();
//
//                    NotificationDto notification = mayBeKafkaNotificationDto.get();
//
//                    assertThat(notification.getContent()).isEqualTo("This is a test message");
//                    assertThat(notification.getId()).isEqualTo(id);
//                });
//    }
//
//}
