package com.social.mcnotification.model;

import com.social.mcnotification.dto.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "author_id", nullable = false)
    private UUID authorId;
    @Column(name = "content")
    private String content;
    @Column(name = "notification_type",
            columnDefinition = "ENUM('LIKE', 'POST', 'POST_COMMENT', 'COMMENT_COMMENT'," +
                    " 'MESSAGE', 'FRIEND_REQUEST', 'FRIEND_BIRTHDAY', 'SEND_EMAIL_MESSAGE')",
            nullable = false)
    private NotificationType notificationType;
    @Column(name = "sent_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime sentTime;
}
