package com.foodygo.service.impl;

import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.mapper.BuildingMapper;
import com.foodygo.mapper.HubMapper;
import com.foodygo.mapper.HubMapperImpl;
import com.foodygo.repository.BuildingRepository;
import com.foodygo.repository.HubRepository;
import com.foodygo.service.spec.HubService;
import com.foodygo.utils.BuildingSpecification;
import com.foodygo.utils.HubSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HubServiceImpl extends BaseServiceImpl<Hub, Integer> implements HubService {

    private final HubRepository hubRepository;
    private final HubMapper hubMapper;
    private final BuildingRepository buildingRepository;
    private final BuildingMapper buildingMapper;

    public HubServiceImpl(HubRepository hubRepository, HubMapper hubMapper, BuildingRepository buildingRepository,
                          BuildingMapper buildingMapper, HubMapperImpl hubMapperImpl) {
        super(hubRepository);
        this.hubRepository = hubRepository;
        this.hubMapper = hubMapper;
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
    }

    @Override
    public PagingResponse getHubsPaging(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = hubRepository.findAll(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all hubs paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(hubMapper::hubToHubDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all hubs paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(hubMapper::hubToHubDTO)
                                .toList())
                        .build();
    }

    @Override
    public PagingResponse getHubsActive(Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = hubRepository.findAllByDeletedFalse(pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all hubs active paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(hubMapper::hubToHubDTO)
                        .toList())
                .build() :
                PagingResponse.builder()
                        .code("Failed")
                        .message("Get all hubs active paging failed")
                        .currentPage(currentPage)
                        .pageSizes(pageSize)
                        .totalElements(pageData.getTotalElements())
                        .totalPages(pageData.getTotalPages())
                        .data(pageData.getContent().stream()
                                .map(hubMapper::hubToHubDTO)
                                .toList())
                        .build();
    }

    @Override
    public HubDTO undeleteHub(Integer hubID) {
        Hub hub = hubRepository.findHubById(hubID);
        if (hub == null) {
            throw new ElementNotFoundException("Hub can not found");
        }
        if (!hub.isDeleted()) {
            throw new UnchangedStateException("Hub is not deleted");
        }
        hub.setDeleted(false);
        return hubMapper.hubToHubDTO(hubRepository.save(hub));
    }

    @Override
    public HubDTO createHub(HubCreateRequest hubCreateRequest) {
        Hub checkExist = hubRepository.findHubByName(hubCreateRequest.getName());
        if (checkExist != null) {
            throw new ElementExistException("There is already a hub with the name " + hubCreateRequest.getName());
        }
        Hub hub = Hub.builder()
                .name(hubCreateRequest.getName())
                .address(hubCreateRequest.getAddress())
                .build();
        return hubMapper.hubToHubDTO(hubRepository.save(hub));
    }

    @Override
    public HubDTO updateHub(HubUpdateRequest hubUpdateRequest, int hubID) {
        Hub hub = hubRepository.findHubById(hubID);
        if (hub != null) {
            if (hubUpdateRequest.getName() != null) {
                if (!hub.getName().equals(hubUpdateRequest.getName())) {
                    Hub checkExist = hubRepository.findHubByName(hubUpdateRequest.getName());
                    if (checkExist != null) {
                        throw new ElementExistException("There is already a hub with the name " + hubUpdateRequest.getName());
                    }
                }
                hub.setName(hubUpdateRequest.getName());
            }
            if (hubUpdateRequest.getAddress() != null) {
                hub.setAddress(hubUpdateRequest.getAddress());
            }
            if(hubUpdateRequest.getDescription() != null) {
                hub.setDescription(hubUpdateRequest.getDescription());
            }
            return hubMapper.hubToHubDTO(hubRepository.save(hub));
        }
        return null;
    }

    @Override
    public PagingResponse getBuildingsByHubID(Integer hubID, Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = buildingRepository.findAllByHub_Id(hubID, pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all building paging by hub ID successfully")
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
                        .message("Get all building paging by hub ID failed")
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
    public List<Order> getOrdersByHubID(Integer hubID) {
        Hub hub = hubRepository.findHubById(hubID);
        if(hub != null) {
            return hub.getOrders();
        }
        return null;
    }

    @Override
    public HubDTO getHubByOrderID(Integer orderID) {
        // chua co OrderService
        return null;
    }

    @Override
    public Hub getHubById(Integer hubID) {
        return hubRepository.findById(hubID).orElseThrow(() -> new IdNotFoundException("Hub not found!"));
    }

    @Override
    public PagingResponse searchHubs(Integer currentPage, Integer pageSize, String name, String sortBy) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        Specification<Hub> spec = Specification.where(null);

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        String searchName = "";
        if (StringUtils.hasText(name)) {
            searchName = name;
        }
        keys.add("name");
        values.add(searchName);

        if(keys.size() == values.size()) {
            for(int i = 0; i < keys.size(); i++) {
                String field = keys.get(i);
                String value = values.get(i);
                Specification<Hub> newSpec = HubSpecification.searchByField(field, value);
                if(newSpec != null) {
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

        var pageData = hubRepository.findAll(spec, pageable);

        return !pageData.getContent().isEmpty() ? PagingResponse.builder()
                .code("Success")
                .message("Get all hubs filter and sort paging successfully")
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(hubMapper::hubToHubDTO)
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
                                .map(hubMapper::hubToHubDTO)
                                .toList())
                        .build();
    }

}
