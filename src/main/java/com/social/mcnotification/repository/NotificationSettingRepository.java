package com.social.mcnotification.repository;

import com.social.mcnotification.model.NotificationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSettingEntity, Integer> {
}
