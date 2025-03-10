package com.foodygo.service.impl;

import com.foodygo.dto.BuildingDTO;
import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.BuildingCreateRequest;
import com.foodygo.dto.request.BuildingUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.dto.response.PublicBuildingDTO;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.mapper.BuildingMapper;
import com.foodygo.mapper.CustomerMapper;
import com.foodygo.mapper.HubMapper;
import com.foodygo.repository.BuildingRepository;
import com.foodygo.repository.CustomerRepository;
import com.foodygo.service.spec.BuildingService;
import com.foodygo.service.spec.HubService;
import com.foodygo.utils.BuildingSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BuildingServiceImpl extends BaseServiceImpl<Building, Integer> implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final HubService hubService;
    private final CustomerRepository customerRepository;
    private final BuildingMapper buildingMapper;
    private final HubMapper hubMapper;
    private final CustomerMapper customerMapper;

    public BuildingServiceImpl(BuildingRepository buildingRepository, HubService hubService, CustomerRepository customerRepository,
                               BuildingMapper buildingMapper, HubMapper hubMapper, CustomerMapper customerMapper) {
        super(buildingRepository);
        this.buildingRepository = buildingRepository;
        this.hubService = hubService;
        this.customerRepository = customerRepository;
        this.buildingMapper = buildingMapper;
        this.hubMapper = hubMapper;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<PublicBuildingDTO> getAllBuildings() {
        List<Building> buildings = buildingRepository.findByDeletedIsFalse();
        return buildings.stream().map(
                PublicBuildingDTO::fromEntity
        ).toList();
    }

    @Override
    public PagingResponse getAllBuildings(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = buildingRepository.findAll(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all buildings paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(buildingMapper::buildingToBuildingDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all buildings paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(buildingMapper::buildingToBuildingDTO)
                                .toList())
                        .build();
    }

    @Override
    public PagingResponse getBuildingsActive(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = buildingRepository.findAllByDeletedFalse(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all buildings active paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(buildingMapper::buildingToBuildingDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all buildings active paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(buildingMapper::buildingToBuildingDTO)
                                .toList())
                        .build();
    }

    @Override
    public BuildingDTO undeleteBuilding(Integer buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building == null) {
            throw new ElementNotFoundException("Hub can not found");
        }
        if (!building.isDeleted()) {
            throw new UnchangedStateException("Hub is not deleted");
        }
        building.setDeleted(false);
        return buildingMapper.buildingToBuildingDTO(buildingRepository.save(building));
    }

    @Override
    public BuildingDTO createBuilding(BuildingCreateRequest buildingCreateRequest) {
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
        } else if (buildingCreateRequest.getHubID() > 0) {
            Hub hub = hubService.findById(buildingCreateRequest.getHubID());
            if (hub != null) {
                building.setHub(hub);
            } else {
                throw new ElementNotFoundException("Hub not found");
            }
        }
        return buildingMapper.buildingToBuildingDTO(buildingRepository.save(building));
    }

    @Override
    public BuildingDTO updateBuilding(BuildingUpdateRequest buildingUpdateRequest, int buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building != null) {
            if (buildingUpdateRequest.getName() != null) {
                if (!building.getName().equals(buildingUpdateRequest.getName())) {
                    Building checkExist = buildingRepository.findBuildingByName(buildingUpdateRequest.getName());
                    if (checkExist != null) {
                        throw new ElementExistException("There is already a building with the name " + buildingUpdateRequest.getName());
                    }
                }
                building.setName(buildingUpdateRequest.getName());
            }
            if (buildingUpdateRequest.getDescription() != null) {
                building.setDescription(buildingUpdateRequest.getDescription());
            }
            if (buildingUpdateRequest.getHubID() == null) {
                building.setHub(null);
            } else if (buildingUpdateRequest.getHubID() > 0) {
                Hub hub = hubService.findById(buildingUpdateRequest.getHubID());
                if (hub != null) {
                    building.setHub(hub);
                } else {
                    throw new ElementNotFoundException("Hub not found");
                }
            }
            return buildingMapper.buildingToBuildingDTO(buildingRepository.save(building));
        }
        return null;
    }

    @Override
    public HubDTO getHubByBuildingID(Integer buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building != null) {
            return hubMapper.hubToHubDTO(building.getHub());
        }
        return null;
    }

    @Override
    public PagingResponse getCustomersByBuildingID(Integer buildingID, Integer currentPage, Integer pageSize) {

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = customerRepository.findAllByBuildingId(buildingID, pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all customers paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(customerMapper::customerToCustomerDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all customers paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(customerMapper::customerToCustomerDTO)
                                .toList())
                        .build();
    }

    @Override
    public BuildingDTO deleteBuilding(Integer buildingID) {
        Building building = buildingRepository.findBuildingById(buildingID);
        if (building == null) {
            throw new ElementNotFoundException("Building not found");
        }
        building.setDeleted(true);
        return buildingMapper.buildingToBuildingDTO(buildingRepository.save(building));
    }

    @Override
    public PagingResponse searchBuildings(Integer currentPage, Integer pageSize, String name, String sortBy) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        Specification<Building> spec = Specification.where(null);

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String searchName = "";
        if (StringUtils.hasText(name)) {
            searchName = name;
        }
        keys.add("name");
        values.add(searchName);

        if (keys.size() == values.size()) {
            for (int i = 0; i < keys.size(); i++) {
                String field = keys.get(i);
                String value = values.get(i);
                Specification<Building> newSpec = BuildingSpecification.searchByField(field, value);
                if (newSpec != null) {
                    spec = spec.and(newSpec);
                }
            }
        }

        List<Sort.Order> orders = new ArrayList<>();
        if (StringUtils.hasText(sortBy)) {
            String sortByLower = sortBy.trim().toLowerCase();

            boolean hasNameASC = sortByLower.contains("nameasc");
            boolean hasNameDESC = sortByLower.contains("namedesc");

            if (hasNameASC ^ hasNameDESC) {
                orders.add(hasNameASC ? Sort.Order.asc("name") : Sort.Order.desc("name"));
            }

        }

        Sort sort = orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);

        pageable = PageRequest.of(currentPage - 1, pageSize, sort);

        var pageData = buildingRepository.findAll(spec, pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all hubs filter and sort paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(buildingMapper::buildingToBuildingDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all hubs filter and sort paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(buildingMapper::buildingToBuildingDTO)
                                .toList())
                        .build();
    }

    @Override
    public List<BuildingDTO> getBuildings() {
        return buildingRepository.findByDeletedIsFalse().stream().map(buildingMapper::buildingToBuildingDTO).collect(Collectors.toList());
    }

}
