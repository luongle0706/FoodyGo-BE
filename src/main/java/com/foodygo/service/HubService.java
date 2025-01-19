package com.foodygo.service;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;

import java.util.List;

public interface HubService extends BaseService<Hub, Integer> {
    List<Hub> getHubsActive();

    Hub undeleteHub(Integer hubID);

    Hub createHub(HubCreateRequest hubCreateRequest);

    Hub updateHub(HubUpdateRequest hubUpdateRequest, int hubID);

    List<Hub> getTest(Building building);
}
