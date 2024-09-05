package com.social.mcnotification.model;

import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid", nullable = false)
    private UUID id;
    @Column(name = "author_id", nullable = false)
    private UUID authorId;
    @Column(name = "receiver_id")
    private UUID receiverId;
    @Column(name = "content")
    private String content;
    @Column(name = "notification_type",
            nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    @Column(name = "service_name")
    @Enumerated(EnumType.STRING)
    private MicroServiceName serviceName;
    @Column(name = "sent_time", columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp sentTime;
    @Column(name = "event_id")
    private UUID eventId;
    @Column(name = "is_readed")
    private Boolean isReaded;
}










