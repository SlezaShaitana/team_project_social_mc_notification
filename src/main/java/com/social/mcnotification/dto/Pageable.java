package com.social.mcnotification.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pageable {
    private Integer page;
    private Integer size;
    private List<String> sort;

    public Pageable(Integer page, Integer size, List<String> sort) {
        if (page <= 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }
        if (size <= 1) {
            throw new IllegalArgumentException("Size must be greater than or equal to 1");
        }
        this.page = page;
        this.size = size;
        this.sort = sort;
    }
}
