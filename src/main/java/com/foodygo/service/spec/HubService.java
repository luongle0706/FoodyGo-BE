package com.foodygo.service.spec;

import com.foodygo.dto.HubDTO;
import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.request.HubCreateRequest;
import com.foodygo.dto.request.HubUpdateRequest;
import com.foodygo.dto.response.PagingResponse;
import com.foodygo.entity.Hub;
import com.foodygo.entity.Order;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;

public interface HubService extends BaseService<Hub, Integer> {

    MappingJacksonValue getHubsForSelection(PagingRequest request);

    PagingResponse getHubsPaging(Integer currentPage, Integer pageSize);

    PagingResponse getHubsActive(Integer currentPage, Integer pageSize);

    List<HubDTO> getHubs();

    HubDTO undeleteHub(Integer hubID);

    HubDTO createHub(HubCreateRequest hubCreateRequest);

    HubDTO updateHub(HubUpdateRequest hubUpdateRequest, int hubID);

    PagingResponse getBuildingsByHubID(Integer hubID, Integer currentPage, Integer pageSize);

    List<Order> getOrdersByHubID(Integer hubID);

    HubDTO getHubByOrderID(Integer orderID);

    Hub getHubById(Integer hubID);

    PagingResponse searchHubs(Integer currentPage, Integer pageSize, String name, String sortBy);

}
