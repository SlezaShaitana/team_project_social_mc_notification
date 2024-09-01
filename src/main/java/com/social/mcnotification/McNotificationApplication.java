package com.social.mcnotification;

import com.social.mcnotification.profile.ProviderProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableFeignClients
@PropertySource("classpath:custom.properties")
@EnableConfigurationProperties(ProviderProperties.class)
public class McNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(McNotificationApplication.class, args);
    }

}
