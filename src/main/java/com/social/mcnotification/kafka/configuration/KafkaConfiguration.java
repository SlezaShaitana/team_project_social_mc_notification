package com.social.mcnotification.kafka.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
//@EnableKafka // ??
public class KafkaConfiguration {

    //notification
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServers;

    @Value("${spring.kafka.kafkaMessageGroupId}")
    private String kafkaMessageGroupId;

    @Value("${spring.kafka.kafkaMessageGroupIdAuth}")
    private String kafkaMassageGroupIdAuth;

    //notification

    @Bean
    public ConsumerFactory<String, NotificationDto> kafkaMessageConsumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMessageGroupId);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }









//    @Bean
//    public ConsumerFactory<String, NotificationDto> KafkaMessageConsumerFactory(ObjectMapper objectMapper) {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//
//        JsonDeserializer<NotificationDto> jsonDeserializer = new JsonDeserializer<>(NotificationDto.class);
//        jsonDeserializer.setRemoveTypeHeaders(false);
//        jsonDeserializer.addTrustedPackages("*");
//        jsonDeserializer.setUseTypeMapperForKey(true);
//
//        ErrorHandlingDeserializer<NotificationDto> errorHandlingDeserializer =
//                new ErrorHandlingDeserializer<>(jsonDeserializer);
//
//        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
//    }
//
//
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationDto> kafkaMessageConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, NotificationDto> kafkaMessageConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, NotificationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaMessageConsumerFactory);

        return factory;
    }
//
//
//
//
//
//    //auth
//
//
//    @Bean
//    public ConsumerFactory<String, RegistrationDto> authKafkaMessageConsumerFactory(ObjectMapper objectMapper) {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//
//        JsonDeserializer<RegistrationDto> jsonDeserializer = new JsonDeserializer<>(RegistrationDto.class);
//        jsonDeserializer.setRemoveTypeHeaders(false);
//        jsonDeserializer.addTrustedPackages("*");
//        jsonDeserializer.setUseTypeMapperForKey(true);
//
//        ErrorHandlingDeserializer<RegistrationDto> errorHandlingDeserializer =
//                new ErrorHandlingDeserializer<>(jsonDeserializer);
//
//        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
//    }















    //auth
    @Bean
    public ConsumerFactory<String, RegistrationDto> authKafkaMessageConsumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMassageGroupIdAuth);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegistrationDto> authKafkaMessageConcurrentKafkaListenerContainerFactory(
            ConsumerFactory<String, RegistrationDto> kafkaMessageConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, RegistrationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaMessageConsumerFactory);

        return factory;
    }



















//    @Bean
//    public ConsumerFactory<String, NotificationDto> kafkaMessageConsumerFactory(ObjectMapper objectMapper) {
//        Map<String, Object> config = new HashMap<>();
//        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
//        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
//        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaMessageGroupId);
//        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//
////        JsonDeserializer<NotificationDto> jsonDeserializer = new JsonDeserializer<>(NotificationDto.class);
////        jsonDeserializer.setRemoveTypeHeaders(false);
////        jsonDeserializer.addTrustedPackages("*");
////        jsonDeserializer.setUseTypeMapperForKey(true);
////
////        ErrorHandlingDeserializer<NotificationDto> errorHandlingDeserializer =
////                new ErrorHandlingDeserializer<>(jsonDeserializer);
////
////        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), errorHandlingDeserializer);
//
//
//        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(objectMapper));
//    }
//

}
