package com.foodygo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopUpHistoryDTO {
    Integer id;
    String description;
    Double amount;
    String time;
    String method;
    Double remaining;
}
