package com.foodygo.service;

import com.foodygo.entity.Building;

import java.util.List;

public interface BuildingService extends BaseService<Building, Integer> {
    List<Building> getBuildingsActive();

    Building undeleteBuilding(Integer buildingID);
}
