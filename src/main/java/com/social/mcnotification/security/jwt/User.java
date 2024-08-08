package com.social.mcnotification.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class User {
    private final UUID id;
    private final String token;
    private final String email;
    private List<String> roles;
}
