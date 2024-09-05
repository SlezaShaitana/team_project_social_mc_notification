package com.social.mcnotification.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class AuthorityDTO {
    private UUID id;
    private String authority;
}
