package com.foodygo.service;

import com.foodygo.dto.request.OrderActivityCreateRequest;
import com.foodygo.dto.response.OrderActivityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderActivityService {
    void logOrderStatusChange(OrderActivityCreateRequest orderActivityCreateRequest);
    Page<OrderActivityResponse> getOrderActivitiesByOrderId(Integer orderId, Pageable pageable);
    void deleteOrderActivitiesByOrderId(Integer orderId);
}
