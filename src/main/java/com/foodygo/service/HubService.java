package com.foodygo.service;

import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Building;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;

import java.util.List;

public interface HubService extends BaseService<Hub, Integer> {

    PagingResponse getHubsPaging(int currentPage, int pageSize);

    List<Hub> getHubsActive();

    Hub undeleteHub(Integer hubID);

    Hub createHub(HubCreateRequest hubCreateRequest);

    Hub updateHub(HubUpdateRequest hubUpdateRequest, int hubID);

    List<Building> getBuildingsByHubID(Integer hubID);

    List<Order> getOrdersByHubID(Integer hubID);

    Hub getHubByOrderID(Integer orderID);

}
