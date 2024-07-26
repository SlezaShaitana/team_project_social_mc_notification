//package com.social.mcnotification.kafka.web;
//
//import com.social.mcnotification.dto.NotificationDto;
//import com.social.mcnotification.kafka.service.KafkaMessageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Service
//@RequestMapping("/api/v1/kafka")
//@RequiredArgsConstructor
//public class KafkaController {
//
//    @Value("${app.kafka.MessageTopic}")
//    private String topicName;
//
//    private KafkaMessageService kafkaMessageService;
//
//    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;
//}
