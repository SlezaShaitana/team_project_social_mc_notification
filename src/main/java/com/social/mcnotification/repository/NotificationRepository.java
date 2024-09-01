package com.social.mcnotification.repository;

import com.social.mcnotification.model.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID>, JpaSpecificationExecutor<NotificationEntity> {
    List<NotificationEntity> findByAuthorId(UUID authorId);

    List<NotificationEntity> findByReceiverId(UUID receiverId);
}
