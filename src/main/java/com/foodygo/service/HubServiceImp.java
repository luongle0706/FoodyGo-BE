package com.foodygo.service;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
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
        Hub hub = Hub.builder()
                .name(hubCreateRequest.getName())
                .block(hubCreateRequest.getBlock())
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
            if (hubUpdateRequest.getBlock() != null) {
                hub.setBlock(hubUpdateRequest.getBlock());
            }
            return hubRepository.save(hub);
        }
        return null;
    }

    @Override
    public List<Hub> getTest(Building building) {
        return hubRepository.findByBuildings(building);
    }

}
