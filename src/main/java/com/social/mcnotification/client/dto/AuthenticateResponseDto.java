package com.social.mcnotification.client.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class AuthenticateResponseDto {
    private final String accessToken;
    private final String refreshToken;
}
