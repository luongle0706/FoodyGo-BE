package com.foodygo.service.spec;

import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse updateOrder(Integer orderId, OrderUpdateRequest orderUpdateRequest);
    OrderResponse getOrderResponseById(Integer id);
    Order getOrderById(Integer id);
    void deleteOrder(Integer id);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    Page<OrderResponse> getAllOrdersByEmployeeId(Integer employeeId, Pageable pageable);
    Page<OrderResponse> getAllOrdersByCustomerId(Integer customerId, Pageable pageable);
    Page<OrderResponse> getAllOrdersByRestaurantId(Integer restaurantId, Pageable pageable);
}
