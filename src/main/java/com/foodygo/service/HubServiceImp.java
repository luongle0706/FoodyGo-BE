package com.foodygo.service;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import com.foodygo.exception.ElementExistException;
import com.foodygo.repository.HubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HubServiceImp extends BaseServiceImp<Hub, Integer> implements HubService {

    private final HubRepository hubRepository;

    public HubServiceImp(HubRepository hubRepository) {
        super(hubRepository);
        this.hubRepository = hubRepository;
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
    public Hub undeleteHub(Integer hubID) {
        Hub hub = hubRepository.findHubById(hubID);
        if (hub != null) {
            if (hub.isDeleted()) {
                hub.setDeleted(false);
                return hubRepository.save(hub);
            }
        }
        return null;
    }

    @Override
    public Hub createHub(HubCreateRequest hubCreateRequest) {
        Hub checkExist = hubRepository.findHubByName(hubCreateRequest.getName());
        if (checkExist != null) {
            throw new ElementExistException("There is already a hub with the name " + hubCreateRequest.getName());
        }
        Hub hub = Hub.builder()
                .name(hubCreateRequest.getName())
                .address(hubCreateRequest.getAddress())
                .build();
        return hubRepository.save(hub);
    }

    @Override
    public Hub updateHub(HubUpdateRequest hubUpdateRequest, int hubID) {
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
            return hubRepository.save(hub);
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
    public Hub getHubByOrderID(Integer orderID) {
        // chua co OrderService
        return null;
    }

}
