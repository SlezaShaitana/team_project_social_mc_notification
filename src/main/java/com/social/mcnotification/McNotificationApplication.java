package com.social.mcnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class McNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(McNotificationApplication.class, args);
	}

}
