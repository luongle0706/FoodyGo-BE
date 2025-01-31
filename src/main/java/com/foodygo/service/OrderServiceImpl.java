package com.foodygo.service;

import com.foodygo.dto.request.*;
import com.foodygo.dto.response.OrderDetailResponse;
import com.foodygo.dto.response.OrderResponse;
import com.foodygo.entity.Order;
import com.foodygo.entity.OrderActivity;
import com.foodygo.entity.OrderDetail;
import com.foodygo.enums.OrderStatus;
import com.foodygo.exception.IdNotFoundException;
import com.foodygo.mapper.OrderDetailMapper;
import com.foodygo.mapper.OrderMapper;
import com.foodygo.repository.HubRepository;
import com.foodygo.repository.OrderDetailRepository;
import com.foodygo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final UserService userService;
    private final CustomerService  customerService;
    private final RestaurantService restaurantService;
    private final ProductService productService;
    private final HubRepository hubRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderActivityService orderActivityService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        Order order = OrderMapper.INSTANCE.toEntity(orderCreateRequest);
        order.setEmployee(userService.findById(orderCreateRequest.getEmployeeId()));
        order.setCustomer(customerService.findById(orderCreateRequest.getCustomerId()));
        order.setRestaurant(restaurantService.getRestaurantById(orderCreateRequest.getRestaurantId()));
        order.setHub(hubRepository.findById(orderCreateRequest.getHubId())
                .orElseThrow(() -> new IdNotFoundException("Hub not found")));

        orderRepository.save(order);

        List<OrderDetail> orderDetails = orderCreateRequest.getOrderDetails().stream()
                .map(dto ->  {
                    OrderDetail orderDetail = OrderDetailMapper.INSTANCE.toEntity(dto);
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(productService.getProductById(dto.getProductId()));
                    return orderDetail;
                }).toList();
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);
        orderRepository.save(order);
        return orderMapper.INSTANCE.toDto(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Integer orderId, OrderUpdateRequest orderUpdateRequest) {
        Order order = getOrderById(orderId);
        OrderStatus oldStatus = order.getStatus();
        OrderMapper.INSTANCE.updateOrderFromDto(orderUpdateRequest, order);
        updateOrderDetails(order, orderUpdateRequest.getOrderDetailUpdateRequests());
        orderRepository.save(order);

        OrderActivityCreateRequest orderActivityCreateRequest = OrderActivityCreateRequest.builder()
                .orderId(orderId)
                .userId(getUserIdFromRequest(orderUpdateRequest))
                .fromStatus(oldStatus)
                .toStatus(orderUpdateRequest.getStatus())
                .image(orderUpdateRequest.getImage())
                .build();

        orderActivityService.logOrderStatusChange(orderActivityCreateRequest);

        return orderMapper.toDto(order);
    }

    private Integer getUserIdFromRequest(OrderUpdateRequest orderUpdateRequest) {
        if (orderUpdateRequest.getUserId() != null) {
            return orderUpdateRequest.getUserId();
        } else if (orderUpdateRequest.getEmployeeId() != null) {
            return orderUpdateRequest.getEmployeeId();
        } else if (orderUpdateRequest.getRestaurantId() != null) {
            return orderUpdateRequest.getRestaurantId();
        } else {
            throw new IdNotFoundException("Not any user found");
        }
    }

    private void updateOrderDetails(Order order, List<OrderDetailUpdateRequest> orderDetailUpdateRequests) {
        Map<Integer, OrderDetail> orderDetailMap = order.getOrderDetails().stream()
                .collect(Collectors.toMap(OrderDetail::getId, orderDetail -> orderDetail));
        List<OrderDetail>  updatedOrderDetails = new ArrayList<>();
        for(OrderDetailUpdateRequest orderDetailUpdateRequest : orderDetailUpdateRequests) {
            if(orderDetailUpdateRequest.getId() == null) {
                OrderDetail newOrderDetail = OrderDetailMapper.INSTANCE.toEntity(orderDetailUpdateRequest);
                newOrderDetail.setProduct(productService.getProductById(orderDetailUpdateRequest.getProductId()));
                updatedOrderDetails.add(newOrderDetail);
            } else {
                OrderDetail oldOrderDetail = orderDetailMap.get(orderDetailUpdateRequest.getId());
                if(oldOrderDetail != null) {
                    OrderDetailMapper.INSTANCE.updateOrderDetailFromDto(orderDetailUpdateRequest, oldOrderDetail);
                    updatedOrderDetails.add(oldOrderDetail);
                    orderDetailMap.remove(orderDetailUpdateRequest.getId());
                }
            }
        }
        orderDetailRepository.deleteAll(orderDetailMap.values());
        orderDetailRepository.saveAll(updatedOrderDetails);
        order.setOrderDetails(updatedOrderDetails);
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new IdNotFoundException("Order not found"));
    }

    @Override
    public void deleteOrder(Integer id) {
        Order order = getOrderById(id);
        List<OrderDetail> orderDetails = order.getOrderDetails();
        if (!orderDetails.isEmpty()) {
            orderDetailRepository.deleteAll(orderDetails);
        }
        List<OrderActivity> orderActivities = order.getOrderActivities();
        if (!orderActivities.isEmpty()) {
            orderActivityService.deleteAllOrderActivitiesByOrderId(orderActivities);
        }
        orderRepository.delete(order);
    }
}
