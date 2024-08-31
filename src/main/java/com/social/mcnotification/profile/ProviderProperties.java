package com.social.mcnotification.profile;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "tprov")
public class ProviderProperties {
    private String profile;
    private String description;
    private String format;
}
