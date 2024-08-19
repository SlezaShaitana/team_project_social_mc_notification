package com.social.mcnotification.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "JwtValidation", url = "http://79.174.80.200:8086/api/v1/auth")
public interface AuthClient {
    @GetMapping("/check-validation")
    Boolean validateToken(@RequestParam("token") String token);
}