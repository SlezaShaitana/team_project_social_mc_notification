package com.social.mcnotification.client.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthenticateResponseDto {
    private final String accessToken;
    private final String refreshToken;
}
