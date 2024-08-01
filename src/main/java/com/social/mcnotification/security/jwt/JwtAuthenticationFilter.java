package com.social.mcnotification.security.jwt;

import com.social.mcnotification.client.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter {
    private final AuthClient authClient;
}
