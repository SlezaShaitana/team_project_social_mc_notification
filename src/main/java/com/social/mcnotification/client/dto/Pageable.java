package com.social.mcnotification.client.dto;

import com.social.mcnotification.dto.Sort;
import lombok.Data;

@Data
public class Pageable {
    private int page;
    private int size;
    private Sort sort;

}
