package com.foodygo.mapper;

import com.foodygo.dto.response.RestaurantResponseDTO;
import com.foodygo.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantResponseMapper {
    RestaurantResponseMapper INSTANCE = Mappers.getMapper(RestaurantResponseMapper.class);
    RestaurantResponseDTO toDTO(Restaurant restaurant);
}
