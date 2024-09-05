package com.social.mcnotification.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Data
public class AuthenticateDto {
    private String email;
    private String password;
}
