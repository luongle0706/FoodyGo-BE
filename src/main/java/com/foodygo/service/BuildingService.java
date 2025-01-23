package com.foodygo.service;

import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Customer;
import com.foodygo.entity.Hub;

import java.util.List;

public interface BuildingService extends BaseService<Building, Integer> {
    List<Building> getBuildingsActive();

    Building undeleteBuilding(Integer buildingID);

    Building createBuilding(BuildingCreateRequest buildingCreateRequest);

    Building updateBuilding(BuildingUpdateRequest buildingUpdateRequest, int buildingID);

    Hub getHubByBuildingID(Integer buildingID);

    PagingResponse getCustomersByBuildingID(Integer buildingID, Integer currentPage, Integer pageSize);

    Building deleteBuilding(Integer buildingID);

}
