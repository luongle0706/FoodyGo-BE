package com.foodygo.dto.response;

import com.foodygo.enums.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionHistoryResponse {
    Integer id;
    String description;
    LocalDateTime time;
    Double amount;
    Double remaining;
    TransactionType type;
}
