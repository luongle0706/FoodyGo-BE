package com.foodygo.dto.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewPagingResponse {
    String code;
    String message;
    int currentPage; // chỉ số page hiện tai: 1, 2, 3, 4...
    int totalPages;
    int pageSize;  // số lượng elements mỗi page
    long totalElements;
    String params;
    String[] sortingOrders;
    Map<String, String> filters;
    Object data;
}
