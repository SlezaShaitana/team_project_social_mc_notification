package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PageableObject {
    private Integer offset;
    private Sort sort;
    private boolean paged;
    private Integer pageSize;
    private boolean unpaged;
    private Integer pageNumber;
}
