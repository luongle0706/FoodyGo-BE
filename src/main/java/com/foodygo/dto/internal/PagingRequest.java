package com.foodygo.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingRequest {
    Integer pageNo;
    Integer pageSize;
    String params;
    String sortBy;
    Map<String, String> filters;
}
