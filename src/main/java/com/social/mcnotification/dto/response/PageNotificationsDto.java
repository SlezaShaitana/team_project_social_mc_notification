package com.social.mcnotification.dto.response;

import com.social.mcnotification.dto.NotificationsDto;
import com.social.mcnotification.dto.SortDto;
import lombok.Builder;
import lombok.Data;

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