//package com.social.mcnotification.kafka;
//
//import com.social.mcnotification.dto.NotificationDto;
//import com.social.mcnotification.kafka.listener.NotificationConsumer;
//import com.social.mcnotification.kafka.listener.NotificationProducer;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.junit.Before;
//import org.junit.ClassRule;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NotificationConsumer.class)
//@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
//@DirtiesContext
//@ActiveProfiles("test")
//public class KafkaTestContainersLiveTest {
//
//    @Autowired
//    private NotificationConsumer consumer;
//
//    @Autowired
//    private NotificationProducer producer;
//
//    @Autowired
//    public KafkaTemplate<String, String> template;
//
//    @ClassRule
//    public static KafkaContainer kafka =
//            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
//
//    @Value("${test.topic}")
//    private String topic;
//
//    @Before
//    public void start() {
//        kafka.start();
//    }
//
//    @Before
//    public void stop() {
//        kafka.stop();
//    }
//
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "baeldung");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        return props;
//    }
//
//
//        @Test
//        public void givenKafkaDockerContainer_whenSendingWithSimpleProducer_thenMessageReceived()
//                throws Exception {
//            String data = "Sending with our own simple KafkaProducer";
//
//            NotificationDto notificationDto = new NotificationDto();
//            notificationDto.setId(UUID.randomUUID());
//            notificationDto.setContent("С днем рождения");
//
//            producer.kafkaTemplate("notification-topic", notificationDto);
//
////            boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
//
//            assertTrue(messageConsumed);
//            assertThat(consumer.getPayload(), containsString(data));
//        }
//
//    }
//
//}
