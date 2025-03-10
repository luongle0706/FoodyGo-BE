package com.foodygo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperatingHourDTO {
    Integer id;
    boolean open;
    boolean hours;
    LocalTime openingTime;
    LocalTime closingTime;
}
