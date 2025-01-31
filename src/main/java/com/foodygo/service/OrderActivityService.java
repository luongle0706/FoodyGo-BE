package com.foodygo.service;

import com.foodygo.dto.request.OrderActivityCreateRequest;
import com.foodygo.dto.response.OrderActivityResponse;
import com.foodygo.entity.OrderActivity;

import java.util.List;

public interface OrderActivityService {
    void logOrderStatusChange(OrderActivityCreateRequest orderActivityCreateRequest);
    List<OrderActivityResponse> getOrderActivitiesByOrderId(Integer orderId);
    void deleteAllOrderActivitiesByOrderId(List<OrderActivity> orderActivities);
}
