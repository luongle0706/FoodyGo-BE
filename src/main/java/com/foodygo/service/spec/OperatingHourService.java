package com.foodygo.service.spec;

import com.foodygo.dto.request.OperatingHourDTO;
import com.foodygo.dto.request.OperatingHourUpdateRequest;
import com.foodygo.entity.OperatingHour;

import java.util.List;

public interface OperatingHourService {

    void createOperatingHour(OperatingHour operatingHour);
    void updateOperatingHour(OperatingHourDTO operatingHour);
    void updateOperatingHoursByRestaurantId(List<OperatingHourDTO> operatingHourDTOS);
    void deleteOperatingHoursByRestaurantId(Integer restaurantId);
    OperatingHour getOperatingHourById(Integer id);
    List<OperatingHour> getOperatingHoursByRestaurantId(Integer restaurantId);
}
