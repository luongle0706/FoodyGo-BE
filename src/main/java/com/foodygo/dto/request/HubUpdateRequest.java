package com.foodygo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HubUpdateRequest {

    @Size(max = 255, min = 1, message = "Name must be between 1 and 255")
    private String name;

    @Size(max = 255, min = 1, message = "Block must be between 1 and 255 characters")
    private String block;

}
