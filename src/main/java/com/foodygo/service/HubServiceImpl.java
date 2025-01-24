package com.foodygo.service;

import com.foodygo.dto.HubDTO;
import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import com.foodygo.exception.ElementExistException;
import com.foodygo.exception.ElementNotFoundException;
import com.foodygo.exception.UnchangedStateException;
import com.foodygo.mapper.BuildingMapper;
import com.foodygo.mapper.HubMapper;
import com.foodygo.repository.BuildingRepository;
import com.foodygo.repository.HubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
                          BuildingMapper buildingMapper) {
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

}
