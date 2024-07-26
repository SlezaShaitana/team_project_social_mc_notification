package com.social.mcnotification.repository;

import com.social.mcnotification.model.NotificationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSettingEntity, String> {
    NotificationSettingEntity findById(UUID id);


}
