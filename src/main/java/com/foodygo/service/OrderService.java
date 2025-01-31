package com.foodygo.service;

import com.foodygo.dto.AddonItemDTO;
import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.AddonItem;
import com.foodygo.entity.Order;
import com.foodygo.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse updateOrder(Integer orderId,OrderUpdateRequest orderUpdateRequest);
    Order getOrderById(Integer id);
    void deleteOrder(Integer id);
}
