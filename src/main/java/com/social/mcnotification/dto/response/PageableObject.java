package com.social.mcnotification.dto.response;

import com.social.mcnotification.dto.SortDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageableObject {

    private Integer offset;
    private SortDto sortDto;
    private Boolean paged;
    private Integer pageSize;
    private Boolean unpaged;
    private Integer pageNumber;
}
