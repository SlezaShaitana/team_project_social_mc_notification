package com.social.mcnotification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "JwtValidation", url = "http://79.174.80.200:8086/api/v1/auth")
public interface JwtValidation {
    @GetMapping("/check-validation")
    Boolean validateToken(@RequestParam("token") String token);
}