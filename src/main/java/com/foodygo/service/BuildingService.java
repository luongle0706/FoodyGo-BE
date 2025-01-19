package com.foodygo.service;

import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.entity.Building;

import java.util.List;

public interface BuildingService extends BaseService<Building, Integer> {
    List<Building> getBuildingsActive();

    Building undeleteBuilding(Integer buildingID);

    Building createBuilding(BuildingCreateRequest buildingCreateRequest);

    Building updateBuilding(BuildingUpdateRequest buildingUpdateRequest, int buildingID);

}
