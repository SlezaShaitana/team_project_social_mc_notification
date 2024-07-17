package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class NotificationSettingDto {
    private UUID id;
    private boolean enableLike;
    private boolean enablePost;
    private boolean enablePostComment;
    private boolean enableCommentComment;
    private boolean enableMessage;
    private boolean enableFriendRequest;
    private boolean enableFriendBirthday;
    private boolean enableSendEmailMessage;

}
