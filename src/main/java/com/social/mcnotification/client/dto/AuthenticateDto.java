package com.social.mcnotification.client.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
public class AuthenticateDto {
    private String email;
    private String password;
}
