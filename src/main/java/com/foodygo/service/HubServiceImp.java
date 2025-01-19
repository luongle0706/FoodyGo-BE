package com.foodygo.service;

import com.foodygo.entity.Hub;
import com.foodygo.repository.HubRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
