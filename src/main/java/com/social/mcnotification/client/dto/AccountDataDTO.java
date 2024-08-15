package com.social.mcnotification.client.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AccountDataDTO {
    private UUID id;
    private boolean isDeleted;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private List<AuthorityDTO> authorities;

}
