package com.social.mcnotification.client.dto;

import com.social.mcnotification.dto.SortDto;
import lombok.Data;

@Data
public class Pageable {
    private int page;
    private int size;
    private SortDto sort;

}
