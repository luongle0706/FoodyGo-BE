package com.foodygo.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {

    private MultipartFile image;

    @Min(value = 1, message = "BuildingID must be positive")
    private int buildingID;

    @Min(value = 1, message = "UserID must be positive")
    private int userID;

}
