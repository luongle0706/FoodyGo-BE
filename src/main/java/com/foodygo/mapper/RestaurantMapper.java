package com.foodygo.mapper;

import com.foodygo.dto.RestaurantDTO;
import com.foodygo.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface RestaurantMapper {

    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);
    RestaurantDTO toDTO(Restaurant restaurant);
}
