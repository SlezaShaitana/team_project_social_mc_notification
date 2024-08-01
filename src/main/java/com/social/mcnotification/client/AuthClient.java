package com.social.mcnotification.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "mc-auth", url = "${database.url}" + "/api/v1/auth")
public interface AuthClient {
    //  public boolean validateToken(String token)
}
