package com.foodygo.mapper;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.entity.AddonItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddonItemMapper {
    AddonItemMapper INSTANCE = Mappers.getMapper(AddonItemMapper.class);

    AddonItemDTO toDTO(AddonItem entity);
}
