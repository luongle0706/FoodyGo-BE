package com.foodygo.service.spec;

import com.foodygo.dto.internal.PagingRequest;
import com.foodygo.dto.request.OrderCreateRequest;
import com.foodygo.dto.request.OrderUpdateRequest;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.dto.response.OrderResponseV2;
import com.foodygo.entity.Order;
import com.foodygo.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.converter.json.MappingJacksonValue;

public interface OrderService {
    int createOrder(OrderCreateRequest orderCreateRequest);
    OrderResponse updateOrder(Integer orderId, OrderUpdateRequest request);
    OrderResponse getOrderResponseById(Integer id);
    OrderResponseV2 getOrderResponseByIdV2(Integer id);
    Order getOrderById(Integer id);
    void deleteOrder(Integer id);
    Page<OrderResponse> getAllOrders(Pageable pageable);
    Page<OrderResponse> getAllOrdersByEmployeeId(Integer employeeId, Pageable pageable);
    Page<OrderResponse> getAllOrdersByCustomerId(Integer customerId, Pageable pageable);
    Page<OrderResponse> getAllOrdersByRestaurantId(Integer restaurantId, Pageable pageable);
    Page<OrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable);
    MappingJacksonValue getOrders(PagingRequest request);
}
