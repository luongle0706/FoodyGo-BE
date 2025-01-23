package com.foodygo.mapper;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuildingMapper {

    @Mapping(source = "id", target = "id")
    BuildingDTO buildingToBuildingDTO(Building building);

    @Mapping(source = "id", target = "id")
    HubDTO hubToHubDTO(Hub hub);
}
