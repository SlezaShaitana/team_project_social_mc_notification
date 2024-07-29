package com.social.mcnotification.client.dto;

import lombok.Data;

@Data
public class FriendSearchDto {
    private String id;
    private boolean isDeleted;
    private String idFrom;
    private StatusCode statusCode;
    private String idTo;
    private StatusCode previousStatusCode;

}
