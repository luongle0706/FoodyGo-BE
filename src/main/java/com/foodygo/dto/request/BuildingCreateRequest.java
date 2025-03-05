package com.foodygo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BuildingCreateRequest {

    @NotBlank(message = "Name is not blank")
    @Size(max = 255, min = 1, message = "Name must be between 1 and 255")
    private String name;

    @Size(max = 255, min = 1, message = "Description must be between 1 and 255 characters")
    private String description;

    private Integer hubID;
}
