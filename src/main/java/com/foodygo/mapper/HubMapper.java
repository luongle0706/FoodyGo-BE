package com.foodygo.mapper;

import com.foodygo.dto.HubDTO;
import com.foodygo.entity.Hub;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HubMapper {

    @Mapping(source = "id", target = "id")
    HubDTO HubToHubDTO(Hub hub);
}
