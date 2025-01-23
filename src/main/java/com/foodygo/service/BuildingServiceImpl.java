package com.foodygo.service;

import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Customer;
import com.foodygo.entity.Hub;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.repository.BuildingRepository;
import com.foodygo.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BuildingServiceImpl extends BaseServiceImpl<Building, Integer> implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final HubService hubService;
    private final CustomerRepository customerRepository;

    public BuildingServiceImpl(BuildingRepository buildingRepository, HubService hubService, CustomerRepository customerRepository) {
        super(buildingRepository);
        this.buildingRepository = buildingRepository;
        this.hubService = hubService;
        this.customerRepository = customerRepository;
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

    @Override
    public Building createBuilding(BuildingCreateRequest buildingCreateRequest) {
        Building checkExist = buildingRepository.findBuildingByName(buildingCreateRequest.getName());
        if (checkExist != null) {
            throw new ElementExistException("There is already a building with the name " + buildingCreateRequest.getName());
        }
        Building building = Building.builder()
                .name(buildingCreateRequest.getName())
                .description(buildingCreateRequest.getDescription())
                .hub(null)
                .build();
        if (buildingCreateRequest.getHubID() == null) {
            building.setHub(null);
        } else if(buildingCreateRequest.getHubID() > 0) {
            Hub hub = hubService.findById(buildingCreateRequest.getHubID());
            if (hub != null) {
                building.setHub(hub);
            } else {
                throw new ElementNotFoundException("Hub not found");
            }
        }
        return buildingRepository.save(building);
    }

    @Override
    public Building updateBuilding(BuildingUpdateRequest buildingUpdateRequest, int buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building != null) {
            if (buildingUpdateRequest.getName() != null) {
                building.setName(buildingUpdateRequest.getName());
            }
            if (buildingUpdateRequest.getDescription() != null) {
                building.setDescription(buildingUpdateRequest.getDescription());
            }
            if (buildingUpdateRequest.getHubID() == null) {
                building.setHub(null);
            } else if(buildingUpdateRequest.getHubID() > 0) {
                    Hub hub = hubService.findById(buildingUpdateRequest.getHubID());
                    if (hub != null) {
                        building.setHub(hub);
                    } else {
                        throw new ElementNotFoundException("Hub not found");
                    }
                }
            return buildingRepository.save(building);
        }
        return null;
    }

    @Override
    public Hub getHubByBuildingID(Integer buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building != null) {
            return building.getHub();
        }
        return null;
    }

    @Override
    public PagingResponse getCustomersByBuildingID(Integer buildingID, Integer currentPage, Integer pageSize) {

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = customerRepository.findAllByBuildingId(buildingID, pageable);

        return PagingResponse.builder()
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public Building deleteBuilding(Integer buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building == null) {
            throw new ElementNotFoundException("Building not found");
        }
        building.setDeleted(true);
        return buildingRepository.save(building);
    }

}
