package com.social.mcnotification.specifications;

import com.social.mcnotification.enums.MicroServiceName;
import com.social.mcnotification.enums.NotificationType;
import com.social.mcnotification.model.NotificationEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class NotificationsSpecifications {

    // * добавить поддержку спецификаций JpaSpecificationExecutor в репозиторий NotificationRepository

    public static Specification<NotificationEntity> notByNotificationTypes(List<NotificationType> types) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.not(root.get("type").in(types));
    }

    public static Specification<NotificationEntity> byNotificationType(NotificationType notificationType) {
        return (Specification<NotificationEntity>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), notificationType);
    }

    public static Specification<NotificationEntity> isReaded(Boolean isReaded) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (isReaded == null) {
                return criteriaBuilder.conjunction(); // Возвращает пустое условие, если isReaded равно null
            }
            return criteriaBuilder.equal(root.get("isReaded"), isReaded);
        };

    }


    public static Specification<NotificationEntity> byMicroServiceName(MicroServiceName serviceName) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (serviceName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("microServiceName"), serviceName);
        };
    }


    public static Specification<NotificationEntity> byAuthorId(UUID authorId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (authorId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("authorId"), authorId);
        };
    }


    public static Specification<NotificationEntity> byReceiverId(UUID receiverId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (receiverId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("receiverId"), receiverId);
        };
    }

    public static Specification<NotificationEntity> bySentTime(Timestamp sentTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (sentTime == null) {
                return criteriaBuilder.conjunction(); // Возвращает пустое условие, если sentTime равно null
            }
            return criteriaBuilder.equal(root.get("sentTime"), sentTime);
        };
    }

    public static Specification<NotificationEntity> afterSentTime(Timestamp sentTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (sentTime == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("sentTime"), sentTime);
        };
    }

    public static Specification<NotificationEntity> beforeSentTime(Timestamp sentTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (sentTime == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("sentTime"), sentTime);
        };
    }

    public static Specification<NotificationEntity> byEventId(UUID eventId) {
        return (Specification<NotificationEntity>) (root, criteriaQuery, criteriaBuilder) -> {
            if (eventId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("eventId"), eventId);

        };

    }
}