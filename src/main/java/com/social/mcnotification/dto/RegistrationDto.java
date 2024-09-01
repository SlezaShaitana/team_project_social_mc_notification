package com.social.mcnotification.dto;

import com.social.mcnotification.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistrationDto {
    private UUID uuid = UUID.randomUUID();
    private boolean deleted;
    private String email;
    private String password1;
    private String password2;
    private String firstName;
    private String lastName;
    private String captchaSecret;
    private Role role = Role.USER;
    private LocalDate reg_date = LocalDate.now();
}
