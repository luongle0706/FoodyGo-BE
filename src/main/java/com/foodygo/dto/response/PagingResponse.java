package com.foodygo.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagingResponse {
    String code;
    String message;
    int currentPage; // chỉ số page hiện tai: 1, 2, 3, 4...
    int totalPages;
    int pageSizes;  // số lượng elements mỗi page
    long totalElements;
    Object data;
}
