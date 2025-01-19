package com.foodygo.service;

import com.foodygo.entity.Hub;

import java.util.List;

public interface HubService extends BaseService<Hub, Integer> {
    List<Hub> getHubsActive();

    Hub undeleteHub(Integer hubID);
}
