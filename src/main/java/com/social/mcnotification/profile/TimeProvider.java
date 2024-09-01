package com.social.mcnotification.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Slf4j
public class TimeProvider implements CommandLineRunner {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${custom.welcomeMessage}")
    private String welcomeMessage;

    private final TimeService timeService;

    private final ProviderProperties providerProperties;

    @Autowired
    public TimeProvider(TimeService timeService, ProviderProperties providerProperties) {
        this.timeService = timeService;
        this.providerProperties = providerProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        Logger.getLogger(TimeProvider.class.getName()).info(" running " + appName
                + " with profile " + providerProperties.getProfile());
        Logger.getLogger(TimeProvider.class.getName()).info(welcomeMessage);
        this.timeService.PrintCurrentTime();

    }
}
