package com.foodygo.service;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;

import java.util.List;

public interface BuildingService extends BaseService<Building, Integer> {
    List<Building> getBuildingsActive();

    BuildingDTO undeleteBuilding(Integer buildingID);

    BuildingDTO createBuilding(BuildingCreateRequest buildingCreateRequest);

    BuildingDTO updateBuilding(BuildingUpdateRequest buildingUpdateRequest, int buildingID);

    HubDTO getHubByBuildingID(Integer buildingID);

    PagingResponse getCustomersByBuildingID(Integer buildingID, Integer currentPage, Integer pageSize);

    BuildingDTO deleteBuilding(Integer buildingID);

}
