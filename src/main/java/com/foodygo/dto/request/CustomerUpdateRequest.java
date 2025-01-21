package com.foodygo.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {

    private String image;

    @Min(value = 1, message = "BuildingID must be positive")
    private int buildingID;

    @Min(value = 1, message = "UserID must be positive")
    private int userID;

}
