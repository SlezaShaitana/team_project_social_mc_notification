package com.social.mcnotification.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Data
@Builder
public class AuthenticateDto {
    private String email;
    private String password;
}
