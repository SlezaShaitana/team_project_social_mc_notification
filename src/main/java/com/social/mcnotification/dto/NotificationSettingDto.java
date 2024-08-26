package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationSettingDto {
    private UUID id;
    private UUID userId;
    private boolean enableLike;
    private boolean enablePost;
    private boolean enablePostComment;
    private boolean enableCommentComment;
    private boolean enableMessage;
    private boolean enableFriendRequest;
    private boolean enableFriendBirthday;
    private boolean enableSendEmailMessage;

}
