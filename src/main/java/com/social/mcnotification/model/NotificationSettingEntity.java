package com.social.mcnotification.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notification_settings")
public class NotificationSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", nullable = false)
    private UUID id;
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "enable_like")
    private boolean enableLike;
    @Column(name = "enable_post")
    private boolean enablePost;
    @Column(name = "enable_post_comment")
    private boolean enablePostComment;
    @Column(name = "enable_comment_comment")
    private boolean enableCommentComment;
    @Column(name = "enable_message")
    private boolean enableMessage;
    @Column(name = "enable_friend_request")
    private boolean enableFriendRequest;
    @Column(name = "enable_friend_birthday")
    private boolean enableFriendBirthday;
    @Column(name = "enable_send_email_message")
    private boolean enableSendEmailMessage;

}
