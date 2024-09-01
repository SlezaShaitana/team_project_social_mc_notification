package com.social.mcnotification.dto.response;

import com.social.mcnotification.dto.NotificationsDto;
import com.social.mcnotification.dto.SortDto;
import lombok.Builder;
import lombok.Data;
//import org.apache.kafka.common.protocol.types.Field;
//import skillbox.group.notification.dto.NotificationsDto;
//import skillbox.group.notification.dto.PageableObject;
//import skillbox.group.notification.dto.Sort;

@Data
@Builder
public class PageNotificationsDto {

    private Integer totalPages;

    private Integer totalElements;

    private Integer number;

    private Integer size;

    private NotificationsDto[] content;

    private SortDto sort;

    private Boolean first;

    private Boolean last;

    private Integer numberOfElements;

    private PageableObject pageable;

    private Boolean empty;

}
