package com.foodygo.dto;

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
    String day;
    boolean open;
    boolean hours;
    LocalTime openingTime;
    LocalTime closingTime;
}
