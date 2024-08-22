package com.social.mcnotification.security.jwt;

import lombok.*;

import java.util.List;
import java.util.UUID;



@RequiredArgsConstructor
@Getter
@Setter
public class UserModel {
    private final UUID id;
    private final String token;
    private final String email;
    private final List<String> roles;
}
