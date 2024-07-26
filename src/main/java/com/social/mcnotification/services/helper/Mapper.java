package com.social.mcnotification.services.helper;

import com.social.mcnotification.dto.NotificationDto;
import com.social.mcnotification.dto.NotificationSettingDto;
import com.social.mcnotification.model.NotificationEntity;
import com.social.mcnotification.model.NotificationSettingEntity;

import java.util.UUID;

public class Mapper {
    public NotificationEntity mapToNotificationEntity(NotificationDto notificationDto) {
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setId(notificationDto.getId());
        notificationEntity.setAuthorId(notificationDto.getAuthorId());
        notificationEntity.setContent(notificationDto.getContent());
        notificationEntity.setNotificationType(notificationDto.getNotificationType());
        notificationEntity.setSentTime(notificationDto.getSentTime());


        notificationEntity.setReceiverId(notificationDto.getReceiverId());
        notificationEntity.setServiceName(notificationDto.getServiceName());
        notificationEntity.setEventId(notificationDto.getEventId());
        notificationEntity.setIsReaded(notificationDto.getIsReaded());
        return notificationEntity;
    }

    public NotificationDto mapToNotificationDto(NotificationEntity notificationEntity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(notificationEntity.getId());
        notificationDto.setAuthorId(notificationEntity.getAuthorId());
        notificationDto.setContent(notificationEntity.getContent());
        notificationDto.setNotificationType(notificationEntity.getNotificationType());
        notificationDto.setSentTime(notificationEntity.getSentTime());

        notificationDto.setReceiverId(notificationEntity.getReceiverId());
        notificationDto.setServiceName(notificationEntity.getServiceName());
        notificationDto.setEventId(notificationEntity.getEventId());
        notificationDto.setIsReaded(notificationEntity.getIsReaded());
        return notificationDto;
    }

    public NotificationSettingEntity mapToSettingEntity(NotificationSettingDto notificationSettingDto) {
        NotificationSettingEntity notificationSettingEntity = new NotificationSettingEntity();
        notificationSettingEntity.setId(notificationSettingDto.getId());
        notificationSettingEntity.setEnableLike(notificationSettingDto.isEnableLike());
        notificationSettingEntity.setEnablePost(notificationSettingDto.isEnablePost());
        notificationSettingEntity.setEnablePostComment(notificationSettingDto.isEnablePostComment());
        notificationSettingEntity.setEnableCommentComment(notificationSettingDto.isEnableCommentComment());
        notificationSettingEntity.setEnableMessage(notificationSettingDto.isEnableMessage());
        notificationSettingEntity.setEnableFriendRequest(notificationSettingDto.isEnableFriendRequest());
        notificationSettingEntity.setEnableFriendBirthday(notificationSettingDto.isEnableFriendBirthday());
        notificationSettingEntity.setEnableSendEmailMessage(notificationSettingDto.isEnableSendEmailMessage());
        return notificationSettingEntity;

    }

    public NotificationSettingDto mapToNotificationSettingDto(NotificationSettingEntity notificationSettingEntity) {
        NotificationSettingDto notificationSettingDto = new NotificationSettingDto();
        notificationSettingDto.setId(notificationSettingDto.getId());
        notificationSettingDto.setEnableLike(notificationSettingEntity.isEnableLike());
        notificationSettingDto.setEnablePost(notificationSettingEntity.isEnablePost());
        notificationSettingDto.setEnablePostComment(notificationSettingEntity.isEnablePostComment());
        notificationSettingDto.setEnableCommentComment(notificationSettingEntity.isEnableCommentComment());
        notificationSettingDto.setEnableMessage(notificationSettingEntity.isEnableMessage());
        notificationSettingDto.setEnableFriendRequest(notificationSettingEntity.isEnableFriendRequest());
        notificationSettingDto.setEnableFriendBirthday(notificationSettingEntity.isEnableFriendBirthday());
        notificationSettingDto.setEnableSendEmailMessage(notificationSettingEntity.isEnableSendEmailMessage());
        return notificationSettingDto;
    }
}
