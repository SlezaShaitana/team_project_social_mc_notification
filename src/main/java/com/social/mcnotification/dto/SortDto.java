package com.social.mcnotification.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
@Data
public class SortDto {
    private boolean empty;
    private boolean unsorted;
    private boolean sorted;
}
