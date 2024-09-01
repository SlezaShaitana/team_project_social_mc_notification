package com.social.mcnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class SortDto {
    private boolean empty;
    private boolean unsorted;
    private boolean sorted;
}
