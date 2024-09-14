//package com.social.mcnotification;
//
//import lombok.experimental.UtilityClass;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.boot.test.util.TestPropertyValues;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.utility.DockerImageName;
//
//@UtilityClass
//public class Kafka {
//    public static final KafkaContainer kafka = new KafkaContainer(
//            DockerImageName.parse("confluentinc/cp-kafka:latest"));
//
//    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        @Override
//        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
//            TestPropertyValues.of(
//                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
//            ).applyTo(applicationContext);
//        }
//    }
//}