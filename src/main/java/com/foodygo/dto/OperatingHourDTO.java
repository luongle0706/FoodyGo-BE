package com.foodygo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "HH:mm")
    LocalTime openingTime;
    @JsonFormat(pattern = "HH:mm")
    LocalTime closingTime;
}
