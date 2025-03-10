package com.foodygo.mapper;

import com.foodygo.dto.OperatingHourDTO;
import com.foodygo.entity.OperatingHour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OperatingHourMapper {
    OperatingHourMapper INSTANCE = Mappers.getMapper(OperatingHourMapper.class);

    @Mapping(source = "open", target = "open")
    @Mapping(source = "24Hours", target = "hours")
    OperatingHourDTO toDto(OperatingHour operatingHour);
}
