package com.social.mcnotification.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class TimeService {
    private final ProviderProperties providerProperties;

    @Autowired
    public TimeService(ProviderProperties providerProperties) {
        this.providerProperties = providerProperties;
    }

    public void PrintCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(providerProperties.getFormat());
        Logger.getLogger(TimeProvider.class.getName()).info(providerProperties.getDescription());
        Logger.getLogger(TimeProvider.class.getName()).info("current time " + dateFormat.format(new Date()));
    }

}