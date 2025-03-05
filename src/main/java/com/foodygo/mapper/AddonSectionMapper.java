package com.foodygo.mapper;

import com.foodygo.dto.AddonSectionDTO;
import com.foodygo.entity.AddonSection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddonSectionMapper {
    AddonSectionMapper INSTANCE = Mappers.getMapper(AddonSectionMapper.class);

    AddonSectionDTO toDto(AddonSection entity);
}
