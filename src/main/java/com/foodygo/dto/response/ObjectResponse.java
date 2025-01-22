package com.foodygo.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectResponse {
    private String status;
    private String message;
    private Object data;
}
