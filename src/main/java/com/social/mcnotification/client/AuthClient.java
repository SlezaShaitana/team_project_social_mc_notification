package com.social.mcnotification.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mc-auth", url = "${database.url}" + "/api/v1/auth")
public interface AuthClient {
    //  public boolean validateToken(String token)

    @PostMapping("/validation")
    public Boolean checkTokenValidity(String token);
    //, UserDetails userDetails, HttpServletRequest request

}
