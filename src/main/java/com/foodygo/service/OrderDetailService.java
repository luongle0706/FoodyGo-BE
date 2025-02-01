package com.foodygo.service;

import com.foodygo.dto.response.OrderDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderDetailService {
    OrderDetailResponse getOrderDetailById(Integer id);
    Page<OrderDetailResponse> getOrderDetailsByOrderId(Integer orderId, Pageable pageable);
    void deleteOrderDetailsByOrderId(Integer orderId);
}
