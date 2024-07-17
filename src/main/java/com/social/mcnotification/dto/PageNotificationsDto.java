package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class PageNotificationsDto {
    private Integer totalPages;
    private Integer totalElements;
    private Integer number;
    private Integer size;
    private List<NotificationsDto> content;
    private Sort sort;
    private boolean first;
    private boolean last;
    private Integer numberOfElements;
    private PageableObject pageable;
    private boolean empty;
}
