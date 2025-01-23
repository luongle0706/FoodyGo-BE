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
import com.foodygo.mapper.HubMapper;
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

    public HubServiceImpl(HubRepository hubRepository, HubMapper hubMapper) {
        super(hubRepository);
        this.hubRepository = hubRepository;
        this.hubMapper = hubMapper;
    }

    @Override
    public PagingResponse getHubsPaging(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = hubRepository.findAll(pageable);

        return PagingResponse.builder()
                .currentPage(currentPage)
                .pageSizes(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public List<Hub> getHubsActive() {
        List<Hub> hubs = hubRepository.findAll();
        List<Hub> activeHubs = new ArrayList<Hub>();
        for (Hub hub : hubs) {
            if(!hub.isDeleted()) {
                activeHubs.add(hub);
            }
        }
        return activeHubs;
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
        return hubMapper.HubToHubDTO(hubRepository.save(hub));
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
        return hubMapper.HubToHubDTO(hubRepository.save(hub));
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
            return hubMapper.HubToHubDTO(hubRepository.save(hub));
        }
        return null;
    }

    @Override
    public List<Building> getBuildingsByHubID(Integer hubID) {
        Hub hub = hubRepository.findHubById(hubID);
        if (hub != null) {
            return hub.getBuildings();
        }
        return null;
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
