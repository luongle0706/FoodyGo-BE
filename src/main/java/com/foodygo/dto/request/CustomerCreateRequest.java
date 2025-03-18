package com.foodygo.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreateRequest {

    private String image;

    @Positive(message = "BuildingID must be positive")
    private Integer buildingID;

    @Positive(message = "UserID must be positive")
    private int userID;

}
