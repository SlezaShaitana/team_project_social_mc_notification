package com.social.mcnotification.client;

import com.social.mcnotification.client.dto.AuthenticateDto;
import com.social.mcnotification.client.dto.AuthenticateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mc-auth", url = "http://79.174.80.200:8086/api/v1/auth")
public interface AuthClient {
    @PostMapping("/login")
    ResponseEntity<AuthenticateResponseDto> login(@RequestBody AuthenticateDto authenticateDto);

}
