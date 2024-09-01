package com.social.mcnotification.kafka.configuration;

import com.social.mcnotification.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(Object object, String topic) {
        kafkaTemplate.send(topic, object);
    }
}

