package com.foodygo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperatingHourUpdateRequest {
    List<OperatingHourDTO> operatingHourList;
}