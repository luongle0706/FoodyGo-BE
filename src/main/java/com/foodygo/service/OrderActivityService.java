package com.foodygo.service;

import com.foodygo.dto.response.OrderActivityResponse;
import com.foodygo.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderActivityService {
    void logOrderStatusChange(Integer orderId, Integer userId, OrderStatus fromStatus, OrderStatus toStatus, String image);
    Page<OrderActivityResponse> getOrderActivitiesByOrderId(Integer orderId, Pageable pageable);

    void deleteOrderActivitiesByOrderId(Integer orderId);
}
