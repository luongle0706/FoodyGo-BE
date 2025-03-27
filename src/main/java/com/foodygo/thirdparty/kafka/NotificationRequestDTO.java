package com.foodygo.thirdparty.kafka;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationRequestDTO {
    Integer userId;
    String title;
    String body;
    String clickAction;
}
