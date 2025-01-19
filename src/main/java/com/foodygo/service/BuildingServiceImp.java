package com.foodygo.service;

import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.repository.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuildingServiceImp extends BaseServiceImp<Building, Integer> implements BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingServiceImp(BuildingRepository buildingRepository) {
        super(buildingRepository);
        this.buildingRepository = buildingRepository;
    }

    @Override
    public List<Building> getBuildingsActive() {
        List<Building> buildings = buildingRepository.findAll();
        List<Building> activeBuildings = new ArrayList<Building>();
        for (Building building : buildings) {
            if(!building.isDeleted()) {
                activeBuildings.add(building);
            }
        }
        return activeBuildings;
    }

    @Override
    public Building undeleteBuilding(Integer buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building != null) {
            if (building.isDeleted()) {
                building.setDeleted(false);
                return buildingRepository.save(building);
            }
        }
        return null;
    }
}
